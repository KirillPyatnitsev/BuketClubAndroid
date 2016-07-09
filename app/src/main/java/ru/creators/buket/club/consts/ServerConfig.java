package ru.creators.buket.club.consts;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class ServerConfig {
    public static final String SERVER_HOST = "buket.club";
    public static final String SERVER_ADRESS = "http://" + SERVER_HOST;
    //public static final String SERVER_ADRESS = "http://bouquet.vexadev.com";
    //public static final String SERVER_ADRESS = "http://176.112.199.34";
    //public static final String SERVER_ADRESS_TEST = "https://6112df6.ngrok.com";
    public static final String SERVER_API_PREFIX = "api";
    public static final String SERVER_API_VERSION = "client";
    public static final String SERVER_API_VERSION_V1 = "v1";


    public static final String SERVER_FAYE = "http://" + SERVER_HOST + ":6840/faye";
    public static final String SERVER_FAYE_ORDER = "/client/orders/order_id/answers";
    public static final String SERVER_FAYE_ORDER_REPLACEMENT = "order_id";

    public static final String FLURRY_API_KEY = "MDW7F3F52TFGVDYG5T9S";
    public static final String MIXPANEL_API_KEY = "33820e1b43766840234930ba41432dd2";

}
