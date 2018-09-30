package indi.liyi.scaffold.utils.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


public class GsonUtil {
    private static Gson sGson;

    private static void checkNotNull(){
        if (sGson == null) {
            sGson = new Gson();
        }
    }

    /**
     * Convert json data to bean
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return bean
     */
    public static <T> T stringToBean(String jsonStr, Class<T> cls) {
        checkNotNull();
        T t = sGson.fromJson(jsonStr, cls);
        return t;
    }

    /**
     * Convert json data to list
     *
     * @param jsonStr
     * @param type
     * @param <T>
     * @return List<?>
     */
    public static <T> List<T> stringToList(String jsonStr, Type type) {
        checkNotNull();
        List<T> list = sGson.fromJson(jsonStr, type);
        return list;
    }

    /**
     * Convert json data to map
     *
     * @param jsonStr
     * @param <T>     Map<String,?>
     * @return Map<String,?>
     */
    public static <T> Map<String, T> stringToMap(String jsonStr) {
        checkNotNull();
        Map<String, T> map = sGson.fromJson(jsonStr, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

    /**
     * Convert json data to the list of the map element
     *
     * @param jsonStr
     * @param <T>
     * @return List<Map>
     */
    public static <T> List<Map<String, T>> stringToListMap(String jsonStr) {
        checkNotNull();
        List<Map<String, T>> list = sGson.fromJson(jsonStr, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    /**
     * Convert object to string
     *
     * @param obj
     * @return String
     */
    public static String objToString(Object obj) {
        checkNotNull();
        return sGson.toJson(obj);
    }
}
