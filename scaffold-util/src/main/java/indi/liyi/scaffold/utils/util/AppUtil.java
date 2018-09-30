package indi.liyi.scaffold.utils.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import indi.liyi.scaffold.utils.constant.AppStatus;

public class AppUtil {
    private static final String TAG = "Scaffold-" + AppUtil.class.getSimpleName();


    /**
     * Return whether the application is alive
     *
     * @param context
     * @param packageName The name of package
     * @return {@code true}: alive <br> {@code false}: dead
     */
    public static boolean isAppAlive(@NonNull Context context, @NonNull String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                LogUtil.i(TAG, String.format("AppAliveInfo ======> App %s is running", packageName));
                return true;
            }
        }
        LogUtil.i(TAG, String.format("AppAliveInfo ======> App %s has been killed", packageName));
        return false;
    }

    /**
     * Return the status of application
     *
     * @param context
     * @param packageName The name of package
     * @return {@link AppStatus#APP_FOREGROUND}: The application is running in the foreground
     * {@link AppStatus#APP_BACKGROUND}: The application is running in the background
     * {@link AppStatus#APP_DEAD}: The application is dead
     */
    @AppStatus
    public int getAppStatus(@NonNull Context context, @NonNull String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo aInfo : processInfos) {
            if (aInfo.processName.equals(packageName)) {
                if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return AppStatus.APP_FOREGROUND;
                } else if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED) {
                    return AppStatus.APP_BACKGROUND;
                }
            }
        }
        return AppStatus.APP_DEAD;
    }

    /**
     * Return whether the app is installed
     *
     * @param context
     * @param packageName
     * @return {@code true}: yes <br> {@code false}: no
     */
    public static boolean isAppInstalled(@NonNull Context context, @NonNull String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName) != null;
    }

    /**
     * Return whether the app is installed.
     *
     * @param context
     * @param action   The Intent action, such as ACTION_VIEW.
     * @param category The desired category.
     * @return {@code true}: yes <br>{ @code false}: no
     */
    public static boolean isAppInstalled(@NonNull Context context, @NonNull String action, @NonNull String category) {
        Intent intent = new Intent(action);
        intent.addCategory(category);
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, 0);
        return info != null;
    }

    /**
     * Launch the application
     *
     * @param context
     * @param packageName The name of the package.
     */
    public static void launchApp(@NonNull Context context, @NonNull String packageName) {
        context.startActivity(getLaunchAppIntent(context, packageName, true));
    }

    /**
     * Launch the application.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If requestCode = 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void launchApp(@NonNull Activity activity, @NonNull String packageName, int requestCode) {
        activity.startActivityForResult(getLaunchAppIntent(activity, packageName), requestCode);
    }

    /**
     * Relaunch the application.
     */
    public static void relaunchApp(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        if (intent == null) return;
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }

    /**
     * Launch the application's details settings
     *
     * @param context The name of the package.
     */
    public static void launchAppDetailsSettings(@NonNull Context context) {
        launchAppDetailsSettings(context, context.getPackageName());
    }

    /**
     * Launch the application's details settings.
     *
     * @param packageName The name of the package.
     */
    public static void launchAppDetailsSettings(@NonNull Context context, @NonNull String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Install the app silently.
     * <p>{@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     * @return {@code true}: success <br> {@code false}: fail
     */
    public static boolean installAppSilent(@NonNull String filePath) {
        return installAppSilent(getFileByPath(filePath), null);
    }

    /**
     * Install the app silently.
     * <p>{@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     * @return {@code true}: success <br> {@code false}: fail
     */
    public static boolean installAppSilent(File file) {
        return installAppSilent(file, null);
    }

    /**
     * Install the app silently.
     * <p>{@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     * @param params   The params of installation(e.g.,<code>-r</code>, <code>-s</code>).
     * @return {@code true}: success <br> {@code false}: fail
     */
    public static boolean installAppSilent(@NonNull String filePath, final String params) {
        return installAppSilent(getFileByPath(filePath), params);
    }

    /**
     * Install the app silently.
     * <p>{@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param file   The file.
     * @param params The params of installation(e.g.,<code>-r</code>, <code>-s</code>).
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean installAppSilent(File file, final String params) {
        return installAppSilent(file, params, isDeviceRooted());
    }

    /**
     * Install the app silently.
     * <p>Without root permission must hold
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param file     The file.
     * @param params   The params of installation(e.g.,<code>-r</code>, <code>-s</code>).
     * @param isRooted True to use root, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean installAppSilent(File file, String params, boolean isRooted) {
        if (!isFileExists(file)) return false;
        String filePath = '"' + file.getAbsolutePath() + '"';
        String command = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm install " +
                (params == null ? "" : params + " ")
                + filePath;
        ShellUtil.CommandResult commandResult = ShellUtil.execCmd(command, isRooted);
        if (commandResult.successMsg != null
                && commandResult.successMsg.toLowerCase().contains("success")) {
            return true;
        } else {
            LogUtil.e(TAG, "installAppSilent successMsg ======> " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg);
            return false;
        }
    }

    /**
     * Uninstall the app.
     *
     * @param packageName The name of the package.
     */
    public static void uninstallApp(@NonNull Context context, @NonNull String packageName) {
        context.startActivity(getUninstallAppIntent(packageName, true));
    }

    /**
     * Uninstall the app.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If requestCode = 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void uninstallApp(@NonNull Activity activity, @NonNull String packageName, int requestCode) {
        activity.startActivityForResult(getUninstallAppIntent(packageName), requestCode);
    }

    /**
     * Uninstall the app silently.
     * <p>{@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>
     *
     * @param packageName The name of the package.
     * @return {@code true}: success <br> {@code false}: fail
     */
    public static boolean uninstallAppSilent(@NonNull String packageName) {
        return uninstallAppSilent(packageName, false);
    }

    /**
     * Uninstall the app silently.
     * {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>
     *
     * @param packageName The name of the package.
     * @param isKeepData  Is keep the data.
     * @return {@code true}: success <br> {@code false}: fail
     */
    public static boolean uninstallAppSilent(@NonNull String packageName, boolean isKeepData) {
        return uninstallAppSilent(packageName, isKeepData, isDeviceRooted());
    }

    /**
     * Uninstall the app silently.
     * <p>{@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>
     *
     * @param packageName The name of the package.
     * @param isKeepData  Is keep the data.
     * @param isRooted    True to use root, false otherwise.
     * @return {@code true}: success <br> {@code false}: fail
     */
    public static boolean uninstallAppSilent(@NonNull String packageName, boolean isKeepData, boolean isRooted) {
        String command = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall "
                + (isKeepData ? "-k " : "")
                + packageName;
        ShellUtil.CommandResult commandResult = ShellUtil.execCmd(command, isRooted);
        if (commandResult.successMsg != null
                && commandResult.successMsg.toLowerCase().contains("success")) {
            return true;
        } else {
            LogUtil.e(TAG, "UninstallAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg);
            return false;
        }
    }

    /**
     * Return whether it is a system application.
     *
     * @param context
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppSystem(@NonNull Context context) {
        return isAppSystem(context, context.getPackageName());
    }

    /**
     * Return whether it is a system application.
     *
     * @param context
     * @param packageName The name of the package.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppSystem(@NonNull Context context, @NonNull String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return the application's signature.
     *
     * @param context
     * @param packageName The name of the package.
     * @return the application's signature
     */
    public static Signature[] getAppSignature(@NonNull Context context, @NonNull String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return the application's signature for SHA1 value.
     *
     * @param context
     * @param packageName The name of the package.
     * @return the application's signature for SHA1 value
     */
    public static String getAppSignatureSHA1(@NonNull Context context, @NonNull String packageName) {
        return getAppSignatureHash(context, packageName, "SHA1");
    }

    /**
     * Return the application's signature for SHA256 value.
     *
     * @param context
     * @param packageName The name of the package.
     * @return the application's signature for SHA256 value
     */
    public static String getAppSignatureSHA256(@NonNull Context context, @NonNull String packageName) {
        return getAppSignatureHash(context, packageName, "SHA256");
    }

    /**
     * Return the application's signature for MD5 value.
     *
     * @param context
     * @param packageName The name of the package.
     * @return the application's signature for MD5 value
     */
    public static String getAppSignatureMD5(@NonNull Context context, @NonNull String packageName) {
        return getAppSignatureHash(context, packageName, "MD5");
    }

    /**
     * Return the application's information
     *
     * @param context
     * @param packageName The name of the package
     * @return The application's information
     */
    public static AppInfo getAppInfo(@NonNull Context context, @NonNull String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return getAppInfo(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return the applications' information.
     *
     * @param context
     * @return the applications' information
     */
    public static List<AppInfo> getAppsInfo(@NonNull Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        // Get information of all application installed in the system
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getAppInfo(pm, pi);
            if (ai != null) {
                list.add(ai);
            }
        }
        return list;
    }

    /**
     * Return the application's uid
     * <p>
     * <p>uid 是应用在安装时系统分配给应用的唯一标识，一个应用只有一个 uid，但是可以有多个 pid，
     * 在应用卸载重装后，系统重新给应用分配 uid</p>
     * <p>注：应用覆盖安装升级时，是不会改变 uid 的，在应用升级时，新应用会读取旧应用的 uid</p>
     *
     * @return uid
     */
    public static int getUid() {
        return android.os.Process.myUid();
    }

    /**
     * Return the application's uid
     *
     * @param context
     * @param packageName The name of the package
     * @return uid
     */
    public static int getUid(@NonNull Context context, @NonNull String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("WrongConstant")
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
            return ai.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Return the applications's uid
     *
     * @return Uid list
     */
    public static List getUids(@NonNull Context context) {
        List<Integer> uidList = new ArrayList<Integer>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        int uid = info.applicationInfo.uid;
                        uidList.add(uid);
                    }
                }
            }
        }
        return uidList;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////  Private Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    private static File getFileByPath(@NonNull String filePath) {
        return new File(filePath);
    }

    private static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    private static Intent getLaunchAppIntent(@NonNull Context context, @NonNull String packageName) {
        return getLaunchAppIntent(context, packageName, false);
    }

    private static Intent getLaunchAppIntent(@NonNull Context context, @NonNull String packageName,boolean isNewTask) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) return null;
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static Intent getUninstallAppIntent(@NonNull String packageName) {
        return getUninstallAppIntent(packageName, false);
    }

    private static Intent getUninstallAppIntent(@NonNull String packageName, boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static String getAppSignatureHash(@NonNull Context context, @NonNull String packageName, @NonNull String algorithm) {
        Signature[] signature = getAppSignature(context, packageName);
        if (signature == null || signature.length <= 0) return "";
        return bytes2HexString(hashTemplate(signature[0].toByteArray(), algorithm))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static AppInfo getAppInfo(PackageManager pm, PackageInfo pi) {
        if (pm == null || pi == null) {
            return null;
        }
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystemApp = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystemApp);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////  AppIno Bean
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public final static class AppInfo {
        private String packageName;
        private String name;
        private Drawable icon;
        private String packagePath;
        private String versionName;
        private int versionCode;
        // Determine whether it is a system application
        private boolean isSystemApp;

        public AppInfo(String packageName, String name, Drawable icon, String packagePath,
                       String versionName, int versionCode, boolean isSystemApp) {
            this.packageName = packageName;
            this.name = name;
            this.icon = icon;
            this.packagePath = packagePath;
            this.versionName = versionName;
            this.versionCode = versionCode;
            this.isSystemApp = isSystemApp;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public void setPackagePath(String packagePath) {
            this.packagePath = packagePath;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public boolean isSystemApp() {
            return isSystemApp;
        }

        public void setSystemApp(boolean systemApp) {
            isSystemApp = systemApp;
        }

        @Override
        public String toString() {
            return "pkg name: " + getPackageName() +
                    "\napp icon: " + getIcon() +
                    "\napp name: " + getName() +
                    "\napp path: " + getPackagePath() +
                    "\napp v name: " + getVersionName() +
                    "\napp v code: " + getVersionCode() +
                    "\nis system: " + isSystemApp();
        }
    }
}
