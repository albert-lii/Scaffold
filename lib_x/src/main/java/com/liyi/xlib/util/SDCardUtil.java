package com.liyi.xlib.util;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SD 卡相关工具类
 */
public final class SDCardUtil {

    private SDCardUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断当前环境下， SD 卡是否正常挂载
     * <p>读取 SD 卡状态为 MEDIA_MOUNTED，SD 卡才能可读可写，所以读取 SD 卡时一般会这样写</p>
     *
     * @return true : 存在<br>false : 不存在
     */
    public static boolean isSDCardEnableByEnvironment() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取当前环境下 SD 卡的路径
     *
     * @return SD 卡的路径
     */
    public static String getSDCardPathByEnvironment() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    /**
     * 是否存在 SD 卡
     *
     * @return true : 存在<br>false : 不存在
     */
    public static boolean isSDCardEnable(@NonNull Context context) {
        return !getSDCardPaths(context).isEmpty();
    }

    /**
     * 获取 SD 卡的路径
     *
     * @param removable 是否可移除，false 返回内置存储卡，true 返回外置 SD 卡
     * @return 存储卡的路径
     */
    public static String getSDCardPath(@NonNull Context context, boolean removable) {
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(sm);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean is_removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable == is_removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有的存储点
     *
     * @return 所有的存储点路径
     */
    public static List<String> getSDCardPaths(@NonNull Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        List<String> paths = new ArrayList<>();
        try {
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);
            Object invoke = getVolumePathsMethod.invoke(storageManager);
            paths = Arrays.asList((String[]) invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }
}
