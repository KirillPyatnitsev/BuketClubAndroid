package ru.creators.buket.club.view.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.gcm.QuickstartPreferences;
import ru.creators.buket.club.gcm.RegistrationIntentService;
import ru.creators.buket.club.model.PriceRange;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.model.Session;
import ru.creators.buket.club.model.lists.ListBouquet;
import ru.creators.buket.club.tools.PreferenceCache;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.BouquetsResponse;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.PriceRangeResponse;
import ru.creators.buket.club.web.response.ProfileResponse;
import ru.creators.buket.club.web.response.SessionResponse;

public class SplashScreenActivity extends BaseActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashScreenActivity";

    private Session session;
    private Profile profile;
    private PriceRange priceRange;
    private ListBouquet listBouquet;

    private int currentAppMode = 100;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d("Tag", "gcm_send_message");
                } else {
                    Log.d("Tag", "token eroor");
                }
            }
        };

//        session = PreferenceCache.getObject(this, PreferenceCache.KEY_SESSION, Session.class);
//        if (session == null){
//            createSession();
//        }else{
//            loadingAllData();
//        }

        assignListener();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_ss_coordinator_root;
    }

    private void createSession(){
        startLoading(false);
        WebMethods.getInstance().createSession(getUniqueDeviceId(), PreferenceCache.getString(this, PreferenceCache.SHAREDPRED_GCM_TOKEN_KEY), new RequestListener<SessionResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
                showSnackBar("Create session error");
            }

            @Override
            public void onRequestSuccess(SessionResponse sessionResponse) {
                saveSession(sessionResponse.getSession());
                session = sessionResponse.getSession();
                stopLoading();
                showSnackBar("Create session done!!!");

                getProfile(session.getAccessToken());
            }
        });
    }

    private void assignListener(){
        getViewById(R.id.a_ss_button_fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp(Profile.TYPE_PRICE_FIX);
            }
        });

        getViewById(R.id.a_ss_button_flex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp(Profile.TYPE_PRICE_FLEXIBLE);
            }
        });
    }

    private void startApp(int profileType){
        currentAppMode = profileType;
        session = PreferenceCache.getObject(this, PreferenceCache.KEY_SESSION, Session.class);
        if (session == null){
            createSession();
        }else{
            getProfile(session.getAccessToken());
        }
    }

    private void saveSession(Session session){
        PreferenceCache.putObject(this, PreferenceCache.KEY_SESSION, session);
    }

    private void loadBouquets(String accessToken){
        startLoading(false);
        WebMethods.getInstance().loadBouquets(accessToken, -1, -1, -1, -1, -1, 1, 200,
                new RequestListener<BouquetsResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                        showSnackBar("Load bouquets error");
                    }

                    @Override
                    public void onRequestSuccess(BouquetsResponse bouquetsResponse) {
                        listBouquet = bouquetsResponse.getListBouquet();
                        stopLoading();
                        showSnackBar("Load bouquets done");
                    }
                });
    }

    private void loadPriceRange(String accessToken){
        startLoading(false);
        WebMethods.getInstance().loadPriceRange(accessToken, new RequestListener<PriceRangeResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
                showSnackBar("Load price range error");
            }

            @Override
            public void onRequestSuccess(PriceRangeResponse priceRangeResponse) {
                priceRange = priceRangeResponse.getPriceRange();
                stopLoading();
                showSnackBar("Load price range done");
            }
        });
    }

    private void getProfile(final String accessToken){
        startLoading(false);
        WebMethods.getInstance().getProfile(accessToken, new RequestListener<ProfileResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
                showSnackBar("Load profile error");
            }

            @Override
            public void onRequestSuccess(ProfileResponse profileResponse) {
                stopLoading();

                profile = profileResponse.getProfile();

                if (currentAppMode != 100 && profile.getTypePriceIndex() != currentAppMode) {
                    generateTypePrice(accessToken);
                } else {
                    loadBouquets(accessToken);
                    loadPriceRange(accessToken);
                }

                showSnackBar("Load profile done");
            }
        });
    }

    private void generateTypePrice(final String accessToken){
        startLoading(false);
        WebMethods.getInstance().generateTypePrice(accessToken, new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
                showSnackBar("Generate type price error");
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                getProfile(accessToken);
                stopLoading();
                showSnackBar("Generate type price done");
            }
        });
    }

    @Override
    protected void allProcessDone() {
        if (session!=null && profile != null && priceRange != null && listBouquet !=null){
            session.setAppMode(profile.getTypePriceIndex());
            saveSession(session);
            DataController.getInstance().setSession(session);
            DataController.getInstance().setListBouquet(listBouquet);
            DataController.getInstance().setPriceRange(priceRange);
            DataController.getInstance().setProfile(profile);

            startActivity(new Intent(this, BucketsActivity.class));
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}