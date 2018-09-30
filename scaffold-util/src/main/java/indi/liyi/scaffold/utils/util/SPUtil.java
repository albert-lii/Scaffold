package indi.liyi.scaffold.utils.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SPUtil {
    private static final String DEF_FILENAME = "Scaffold-SP";
    private static final int DEF_MODE = Context.MODE_PRIVATE;

    private static Map<String, SPUtil> sInstanceMap;
    private static SharedPreferences sSp;
    private static SharedPreferences.Editor sEditor;

    private SPUtil(@NonNull Context context, @NonNull String fileName, int mode) {
        super();
        sSp = context.getSharedPreferences(fileName, mode);
        sEditor = sSp.edit();
    }

    public static SPUtil getInstance(@NonNull Context context) {
        return getInstance(context, DEF_FILENAME, DEF_MODE);
    }

    public static SPUtil getInstance(@NonNull Context context, @NonNull String fileName) {
        return getInstance(context, fileName, DEF_MODE);
    }

    public static SPUtil getInstance(@NonNull Context context, @NonNull String fileName, @NonNull int mode) {
        if (sInstanceMap == null) {
            sInstanceMap = new HashMap<String, SPUtil>();
        }
        // Get the sharedpreferences object first
        SPUtil manager = sInstanceMap.get(fileName + "_" + mode);
        // If you can't get it, recreate the sharedpreferences object
        if (manager == null) {
            manager = new SPUtil(context, fileName, mode);
            sInstanceMap.put(fileName + "_" + mode, manager);
        }
        return manager;
    }

    /**
     * Sava data
     *
     * @param key
     * @param object Stored content
     */
    public void put(@NonNull String key, Object object) {
        if (object instanceof String) {
            sEditor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            sEditor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            sEditor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            sEditor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            sEditor.putLong(key, (Long) object);
        } else {
            sEditor.putString(key, object.toString());
        }
        sEditor.commit();
    }

    /**
     * Save multiple pieces of data at the same time
     *
     * @param map Stored data
     */
    public void add(Map<String, Object> map) {
        Set<String> set = map.keySet();
        for (String key : set) {
            Object object = map.get(key);
            if (object instanceof String) {
                sEditor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                sEditor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                sEditor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                sEditor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                sEditor.putLong(key, (Long) object);
            } else {
                sEditor.putString(key, object.toString());
            }
        }
        sEditor.commit();
    }

    /**
     * Get the stored data
     *
     * @param key
     * @param object Default return value
     * @return The stored data
     */
    public Object get(String key, Object object) {
        if (object instanceof String) {
            return sSp.getString(key, (String) object);
        } else if (object instanceof Integer) {
            return sSp.getInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            return sSp.getBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            return sSp.getFloat(key, (Float) object);
        } else if (object instanceof Long) {
            return sSp.getLong(key, (Long) object);
        }
        return null;
    }

    /**
     * Delete data based on key
     *
     * @param key
     */
    public void delete(String key) {
        sEditor.remove(key);
        sEditor.commit();
    }

    /**
     * Clear all data
     */
    public void clear() {
        sEditor.clear();
        sEditor.commit();
    }

    public SharedPreferences.Editor getEditor() {
        return sEditor;
    }
}
