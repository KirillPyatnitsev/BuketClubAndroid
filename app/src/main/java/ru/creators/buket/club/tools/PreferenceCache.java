package ru.creators.buket.club.tools;

import android.content.Context;
import android.content.SharedPreferences;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public final class PreferenceCache {

    public static final String KEY_SESSION= "key_session";
    private final static String mFileName = "buket.club";
    public static final String SHAREDPRED_GCM_TOKEN_KEY = "itsezzy_gcm_token_key";
    private static ObjectMapper objectMapper = new ObjectMapper();

    private PreferenceCache() {
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getBoolean(key, false);
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.getString(key, null);
    }

    public static <T> T getObject(Context context, String key, Class<T> clazz){
        String json = getString(context, key);

        T object = null;

        if (json!=null){
            try {
                object = toObject(json, clazz);
            }catch (IOException error){
                error.printStackTrace();
            }
        }

        return object;
    }

    public static void putObject(Context context, String key, Object object){
        try {
            String json = toJson(object);
            putString(context, key, json);
        }catch (IOException error){
            error.printStackTrace();
        }
    }

    public static void removeKey(Context context, String key){
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.edit();
    }

    private static String toJson(Object object) throws IOException {
        String json = objectMapper.writeValueAsString(object);

        return json;
    }

    private static <T> T toObject(String json, Class<T> clazz) throws IOException {
        T object = objectMapper.readValue(json, clazz);
        return object;
    }
}
