package indi.liyi.scaffold.utils.constant;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        NetworkType.NETWORK_NO,
        NetworkType.NETWORK_UNKNOWN,
        NetworkType.NETWORK_WIFI,
        NetworkType.NETWORK_2G,
        NetworkType.NETWORK_3G,
        NetworkType.NETWORK_4G,
        NetworkType.NETWORK_ETHERNET
})
@Retention(RetentionPolicy.SOURCE)
public @interface NetworkType {
    /**
     * No network
     */
    int NETWORK_NO = -1;
    /**
     * Unknow
     */
    int NETWORK_UNKNOWN = 0;
    /**
     * Wifi
     */
    int NETWORK_WIFI = 1;
    /**
     * 2G
     */
    int NETWORK_2G = 2;
    /**
     * 3G
     */
    int NETWORK_3G = 3;
    /**
     * 4G
     */
    int NETWORK_4G = 4;
    /**
     * Ethernet
     */
    int NETWORK_ETHERNET = 5;
}
