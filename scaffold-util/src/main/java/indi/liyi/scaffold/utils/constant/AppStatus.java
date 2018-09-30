package indi.liyi.scaffold.utils.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        AppStatus.APP_DEAD,
        AppStatus.APP_FOREGROUND,
        AppStatus.APP_BACKGROUND
})
@Retention(RetentionPolicy.SOURCE)
public @interface AppStatus {
    /**
     * The application is dead.
     */
    int APP_DEAD = 0;

    /**
     * The application is running in the foreground.
     */
    int APP_FOREGROUND = 1;

    /**
     * The application is running in the background.
     */
    int APP_BACKGROUND = 2;
}
