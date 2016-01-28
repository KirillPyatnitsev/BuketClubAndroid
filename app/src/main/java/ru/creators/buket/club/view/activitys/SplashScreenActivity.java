package ru.creators.buket.club.view.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
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

    private boolean TEST_APPLICATION_MODE = true;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashScreenActivity";

    private Session session;
    private Profile profile;
    private PriceRange priceRange;
    private ListBouquet listBouquet;

    private int currentAppMode = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);




        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean sentToken = sharedPreferences
                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

        if (checkPlayServices() && !sentToken) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        if (TEST_APPLICATION_MODE) {
            assignListener();
        }else{
            getViewById(R.id.a_ss_linear_buttons_container).setVisibility(View.GONE);
            if (sentToken)
                startApp(currentAppMode);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(gcmRegistrationDone,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_ss_coordinator_root;
    }

    private BroadcastReceiver gcmRegistrationDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TEST_APPLICATION_MODE)
                startApp(currentAppMode);
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gcmRegistrationDone);
        super.onPause();
    }

    private void createSession(){
        startLoading(false);
        WebMethods.getInstance().createSession(getUniqueDeviceId(), PreferenceCache.getString(this, PreferenceCache.SHAREDPRED_GCM_TOKEN_KEY), new RequestListener<SessionResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(SessionResponse sessionResponse) {
                saveSession(sessionResponse.getSession());
                session = sessionResponse.getSession();
                stopLoading();

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
                    }

                    @Override
                    public void onRequestSuccess(BouquetsResponse bouquetsResponse) {
                        listBouquet = bouquetsResponse.getListBouquet();
                        stopLoading();
                    }
                });
    }

    private void loadPriceRange(String accessToken){
        startLoading(false);
        WebMethods.getInstance().loadPriceRange(accessToken, new RequestListener<PriceRangeResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(PriceRangeResponse priceRangeResponse) {
                priceRange = priceRangeResponse.getPriceRange();
                stopLoading();
            }
        });
    }

    private void getProfile(final String accessToken){
        startLoading(false);
        WebMethods.getInstance().getProfile(accessToken, new RequestListener<ProfileResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
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
            }
        });
    }

    private void generateTypePrice(final String accessToken){
        startLoading(false);
        WebMethods.getInstance().generateTypePrice(accessToken, new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                getProfile(accessToken);
                stopLoading();
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