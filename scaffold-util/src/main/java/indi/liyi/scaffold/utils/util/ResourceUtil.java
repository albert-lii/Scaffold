package indi.liyi.scaffold.utils.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ResourceUtil {

    public static int getLayoutIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "layout", resName);
    }

    /**
     * Return the resource id of layout by name
     *
     * @param packageName The name of package
     * @param resName     The name of layout's xml
     * @return the resource id of layout
     */
    public static int getLayoutIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "layout", resName);
    }

    public static int getViewIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "id", resName);
    }

    /**
     * Return the resource id of view by name
     *
     * @param packageName The name of package
     * @param resName     The name of view's id
     * @return the resource id of view
     */
    public static int getViewIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "id", resName);
    }

    public static int getDrawableIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "drawable", resName);
    }

    /**
     * Return the resource id of drawable by name
     *
     * @param packageName The name of package
     * @param resName     The name of drawable's id
     * @return the resource id of drawable
     */
    public static int getDrawableIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "drawable", resName);
    }

    public static int getColorIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "color", resName);
    }

    /**
     * Return the resource id of color by name
     *
     * @param packageName The name of package
     * @param resName     The name of color's id
     * @return the resource id of color
     */
    public static int getColorIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "color", resName);
    }

    public static int getDimenIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "dimen", resName);
    }

    /**
     * Return the resource id of dimen by name
     *
     * @param packageName The name of package
     * @param resName     The name of dimen's id
     * @return the resource id of dimen
     */
    public static int getDimenIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "dimen", resName);
    }

    public static int getStringIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "string", resName);
    }

    /**
     * Return the resource id of string by name
     *
     * @param packageName The name of package
     * @param resName     The name of string's id
     * @return the resource id of string
     */
    public static int getStringIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "string", resName);
    }

    public static int getStringArrayIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "array", resName);
    }

    public static int getStringArrayIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "array", resName);
    }

    public static int getStyleIdByName(@NonNull Context context, @Nullable String resName) {
        return getIdByName(context.getPackageName(), "style", resName);
    }

    /**
     * Return the resource id of style by name
     *
     * @param packageName The name of package
     * @param resName     The name of style's id
     * @return the resource id of style
     */
    public static int getStyleIdByName(@NonNull String packageName, @Nullable String resName) {
        return getIdByName(packageName, "style", resName);
    }

    /**
     * Return the resource id by resource name
     *
     * @param packageName The name of package
     * @param className   The name of resource type
     * @param resName     The name of resource
     * @return recource id
     */
    public static int getIdByName(@NonNull String packageName, @Nullable String className, @Nullable String resName) {
        int id = 0;
        try {
            Class r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (Class cls : classes) {
                if (cls.getName().split("\\$")[1].equals(className)) {
                    desireClass = cls;
                    break;
                }
            }
            if (desireClass != null) {
                id = desireClass.getField(resName).getInt(desireClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
