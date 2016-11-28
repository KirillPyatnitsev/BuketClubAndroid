package com.opendev.buket.club.consts;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class ServerConfig {

    public static final boolean USE_FAKE_DEBUG_DATA = false;
    public static final String TAG_PREFIX = "buket_";

    public static final String SERVER_HOST = "buket.club";
    public static final String SERVER_HOST2 = "test.paymentgate.ru/testpayment/rest";
    public static final String SERVER_HOST_GOOGLE = "https://maps.googleapis.com/maps/api/geocode/";
    public static final String SERVER_ADDRESS = "http://" + SERVER_HOST;
    public static final String SERVER_ADDRESS2 = "https://" + SERVER_HOST2;
    public static final String SERVER_API_PREFIX = "api";
    public static final String SERVER_API_VERSION = "client";
    public static final String SERVER_API_VERSION_V1 = "v1";
    public static final int BUKET_CLUB_PROJECT_ID = 1;


    public static final String SERVER_FAYE = "http://" + SERVER_HOST + ":6840/faye";
    public static final String SERVER_FAYE_ORDER = "/client/orders/order_id/answers";
    public static final String SERVER_FAYE_ORDER_REPLACEMENT = "order_id";

    public static final String FLURRY_API_KEY = "MDW7F3F52TFGVDYG5T9S";
    public static final String MIXPANEL_API_KEY = "33820e1b43766840234930ba41432dd2";

    public static final String YANDEX_KASSA_CLIENT_ID = "14DAD9CD81BCAEA3C7390D870D558C43310DBA7DE3D7FB6C7D1F1AECF306C7AB";
    public static final String YANDEX_KASSA_P2P = "410014452095983";

}
