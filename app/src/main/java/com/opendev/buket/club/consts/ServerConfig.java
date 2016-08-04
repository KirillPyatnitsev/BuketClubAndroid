package com.opendev.buket.club.consts;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class ServerConfig {

    public static final boolean USE_FAKE_DEBUG_DATA = false;
    public static final String TAG_PREFIX = "buket_";

    public static final String SERVER_HOST = "buket.club";
    public static final String SERVER_ADDRESS = "http://" + SERVER_HOST;
    public static final String SERVER_API_PREFIX = "api";
    public static final String SERVER_API_VERSION = "client";
    public static final String SERVER_API_VERSION_V1 = "v1";


    public static final String SERVER_FAYE = "http://" + SERVER_HOST + ":6840/faye";
    public static final String SERVER_FAYE_ORDER = "/client/orders/order_id/answers";
    public static final String SERVER_FAYE_ORDER_REPLACEMENT = "order_id";

    public static final String FLURRY_API_KEY = "MDW7F3F52TFGVDYG5T9S";
    public static final String MIXPANEL_API_KEY = "33820e1b43766840234930ba41432dd2";

    public static final String GCM_DEFAULT_SENDER_ID = "915505288420";

    public static final String YANDEX_KASSA_CLIENT_ID = "CF81271080DB5D1AC2C1659FA16962AAD09FCEEBF3D3DF88DF32B7FD243EE77D";
    public static final String YANDEX_KASSA_P2P = "410013897372739";

}
