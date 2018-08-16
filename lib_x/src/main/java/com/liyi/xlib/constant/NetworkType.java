package com.liyi.xlib.constant;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        NetworkType.NETWORK_NO,
        NetworkType.NETWORK_UNKNOWN,
        NetworkType.NETWORK_WIFI,
        NetworkType.NETWORK_2G,
        NetworkType.NETWORK_3G,
        NetworkType.NETWORK_4G
})
@Retention(RetentionPolicy.SOURCE)
public @interface NetworkType {
    /**
     * 没有网络
     */
    int NETWORK_NO = -1;
    /**
     * 未知网络
     */
    int NETWORK_UNKNOWN = 0;
    /**
     * wifi
     */
    int NETWORK_WIFI = 1;
    /**
     * 2G 网
     */
    int NETWORK_2G = 2;
    /**
     * 3G 网
     */
    int NETWORK_3G = 3;
    /**
     * 4G 网
     */
    int NETWORK_4G = 4;
}
