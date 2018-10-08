package indi.liyi.scaffold.utils.util;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationUtil {
    private static final String TAG = "Scaffold-" + LocationUtil.class.getSimpleName();
    private static final int TIME_LIMIT = 1000 * 60 * 2;

    private static LocationManager sLocationManager;
    private static MyLocationListener sLocationListener;
    private static OnLocationChangeListener sChangedListener;


    /**
     * Return whether the gps is enabled
     *
     * @param context
     * @return {@code true}: enabled <br> {@code false}: disabled
     */
    public static boolean isGpsEnabled(@NonNull Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Return whether the location is enabled
     *
     * @param context
     * @return {@code true}: enabled <br>{@code false}: disabled
     */
    public static boolean isLocationEnabled(@NonNull Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Open the Gps Setting Interface
     *
     * @param context
     */
    public static void openGpsSettings(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Register location monitoring
     * <p>Remember to call {@link #unregister()} after using</p>
     * <p>{@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     * <p>{@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />}</p>
     * <p>{@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />}</p>
     * <p>If {@code minDistance} equals 0, update periodically by {@code minTime}</p>
     * <p>If {@code minDistance} doesn't equal 0, take {@code minDistance} as the criterion</p>
     * <p>If both {@code minTime} and {@code minDistance} are 0, update at any time.</p>
     *
     * @param context
     * @param minTime     The update period of location information (Unit: milliseconds)
     * @param minDistance The minimum distance of location change:
     *                    The location information will be updated when the value of the location distance changes beyond the minDistance. (Unit: metre)
     * @param listener    The callback interface for listening for location updates
     * @return {@code true}: success <br> {@code false}: fail
     */
    @RequiresPermission(ACCESS_FINE_LOCATION)
    public static boolean register(@NonNull Context context, long minTime, long minDistance, OnLocationChangeListener listener) {
        if (listener == null) return false;
        sLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // noinspection ConstantConditions
        if (!sLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && !sLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LogUtil.d(TAG, "无法定位，请打开定位服务");
            return false;
        }
        sChangedListener = listener;
        String provider = sLocationManager.getBestProvider(getCriteria(), true);
        Location location = sLocationManager.getLastKnownLocation(provider);
        if (location != null) listener.getLastKnownLocation(location);
        if (sLocationListener == null) sLocationListener = new MyLocationListener();
        sLocationManager.requestLocationUpdates(provider, minTime, minDistance, sLocationListener);
        return true;
    }

    /**
     * Unregister location monitoring
     */
    @RequiresPermission(ACCESS_COARSE_LOCATION)
    public static void unregister() {
        if (sLocationManager != null) {
            if (sLocationListener != null) {
                sLocationManager.removeUpdates(sLocationListener);
                sLocationListener = null;
            }
            sLocationManager = null;
        }
        if (sChangedListener != null) {
            sChangedListener = null;
        }
    }

    /**
     * Return the location parameters
     *
     * @return {@link Criteria}
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE 比较粗略，Criteria.ACCURACY_FINE 则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * Return the geolocation by latitude and longitude
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return {@link Address}
     */
    public static Address getAddress(@NonNull Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the country name by latitude and longitude
     *
     * @param latitude
     * @param longitude
     * @return the country name
     */
    public static String getCountryName(@NonNull Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * Return the locality by latitude and longitude
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return the locality
     */
    public static String getLocality(@NonNull Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * Return the street name by latitude and longitude
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return the street name
     */
    public static String getStreet(@NonNull Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    /**
     * Return whether it is a better location
     *
     * @param newLocation         The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     * @return {@code true}: yes <br> {@code false}: no
     */
    public static boolean isBetterLocation(Location newLocation, Location currentBestLocation) {
        if (newLocation != null && currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        } else if (newLocation == null && currentBestLocation != null) {
            return false;
        } else if (newLocation == null && currentBestLocation == null) {
            return false;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_LIMIT;
        boolean isSignificantlyOlder = timeDelta < -TIME_LIMIT;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether they are the same provider
     *
     * @param provider1
     * @param provider2
     * @return {@code true}: yes <br> {@code false}: false
     */
    public static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private static class MyLocationListener implements LocationListener {
        /**
         * Trigger this function when the coordinates change,
         * and if the provider passes in the same coordinates, it will not be triggered
         *
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            if (sChangedListener != null) {
                sChangedListener.onLocationChanged(location);
            }
        }

        /**
         * provider 的可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (sChangedListener != null) {
                sChangedListener.onStatusChanged(provider, status, extras);
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    LogUtil.d(TAG, "当前GPS状态为可见状态");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    LogUtil.d(TAG, "当前GPS状态为服务区外状态");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    LogUtil.d(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * provider 被 enable 时触发此函数，比如 GPS 被打开
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * provider 被 disable 时触发此函数，比如 GPS 被关闭
         */
        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    public interface OnLocationChangeListener {

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        void getLastKnownLocation(Location location);

        /**
         * 当坐标改变时触发此函数，如果 Provider 传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        void onLocationChanged(Location location);

        /**
         * provider的 可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        void onStatusChanged(String provider, int status, Bundle extras);// 位置状态发生改变
    }
}
