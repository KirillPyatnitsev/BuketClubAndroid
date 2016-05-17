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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.transitionseverywhere.TransitionManager;

import java.util.Timer;
import java.util.TimerTask;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.gcm.QuickstartPreferences;
import ru.creators.buket.club.gcm.RegistrationIntentService;
import ru.creators.buket.club.model.PriceRange;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.model.Session;
import ru.creators.buket.club.model.lists.ListBouquet;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.tools.PreferenceCache;
import ru.creators.buket.club.view.custom.maskededittext.MaskedEditText;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.BouquetsResponse;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.PhoneCodeResponse;
import ru.creators.buket.club.web.response.PriceRangeResponse;
import ru.creators.buket.club.web.response.ProfileResponse;
import ru.creators.buket.club.web.response.SessionResponse;

public class SplashScreenActivity extends BaseActivity {

    private boolean TEST_APPLICATION_MODE = false;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashScreenActivity";

    private Session session;
    private Profile profile;
    private PriceRange priceRange;
    private ListBouquet listBouquet;

    private int currentAppMode = 100;

    private Button buttonFlexible;
    private Button buttonFix;
    private Button buttonNext;
    private TextView textResendCode;

    private EditText editName;
    private EditText editCode;
    private MaskedEditText editPhone;

    private String phone;

    private Timer timer;

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

        Log.d("Log", getUniqueDeviceId());

        assignView();
        assignListener();

        if (TEST_APPLICATION_MODE) {
            buttonFix.setVisibility(View.VISIBLE);
            buttonFlexible.setVisibility(View.VISIBLE);
        } else {
            if (sentToken)
                startApp(currentAppMode);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(gcmRegistrationDone,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

        FlurryAgent.logEvent("BucketsActivity");
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

    private void assignView() {
        buttonFix = getViewById(R.id.a_ss_button_fix);
        buttonFlexible = getViewById(R.id.a_ss_button_flex);
        buttonNext = getViewById(R.id.a_ss_button_enter);
        textResendCode = getViewById(R.id.a_ss_text_resend);

        editName = getViewById(R.id.a_ss_edit_name);
        editCode = getViewById(R.id.a_ss_edit_code);
        editPhone = getViewById(R.id.a_ss_edit_phone);
    }

    private void createSession() {
        startLoading(false);
        PreferenceCache.removeKey(this, PreferenceCache.KEY_SESSION);
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

    private void assignListener() {
        buttonFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp(Profile.TYPE_PRICE_FIX);
            }
        });

        buttonFlexible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp(Profile.TYPE_PRICE_FLEXIBLE);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        textResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = null;
                next();
                TransitionManager.beginDelayedTransition(getCoordinatorLayout());
                textResendCode.setVisibility(View.GONE);
            }
        });
    }

    private void next() {
        String name = editName.getText().toString();
        String phone = editPhone.getText().toString();
        if (this.phone == null || !this.phone.equals(phone)) {
            if (name.isEmpty()) {
                showSnackBar(R.string.name_not_entered);
            } else if (!Helper.phoneVerification(phone)) {
                showSnackBar(R.string.phone_not_entered);
            } else {
                this.phone = phone;
                phoneVerificationStartPostRequest(phone, name);
            }
        } else {
            String code = editCode.getText().toString();

            Profile profile = new Profile();
            profile.setPhone(phone);
            profile.setFillName(name);
            profile.setCode(code);
            profilePatchRequest(profile);
        }
    }

    private void startApp(int profileType) {
        currentAppMode = profileType;
        session = PreferenceCache.getObject(this, PreferenceCache.KEY_SESSION, Session.class);
        if (session == null) {
            createSession();
        } else {
            getProfile(session.getAccessToken());
        }
    }

    private void saveSession(Session session) {
        PreferenceCache.putObject(this, PreferenceCache.KEY_SESSION, session);
    }

    private void loadBouquets(String accessToken) {
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

    private void loadPriceRange(String accessToken) {
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

    private void getProfile(final String accessToken) {
        startLoading(false);
        WebMethods.getInstance().getProfile(accessToken, new RequestListener<ProfileResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                createSession();
                stopLoading();
            }

            @Override
            public void onRequestSuccess(ProfileResponse profileResponse) {
                stopLoading();

                profile = profileResponse.getProfile();

//                if (currentAppMode != 100 && profile.getTypePriceIndex() != currentAppMode) {
                if (profile.getTypePriceIndex() != Profile.TYPE_PRICE_FIX) {
                    generateTypePrice(accessToken);
                } else {
                    loadBouquets(accessToken);
                    loadPriceRange(accessToken);
                }
            }
        });
    }

    private void generateTypePrice(final String accessToken) {
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
        if (session != null && profile != null && priceRange != null && listBouquet != null) {
            session.setAppMode(profile.getTypePriceIndex());
            saveSession(session);
            DataController.getInstance().setSession(session);
            DataController.getInstance().setListBouquet(listBouquet);
            DataController.getInstance().setPriceRange(priceRange);
            DataController.getInstance().setProfile(profile);

            if (profile.getPhone() != null && !profile.getPhone().isEmpty())
                goToBuketActivity();
            else
                showRegistration();
        }
    }

    private void goToBuketActivity() {
        startActivity(new Intent(this, BucketsActivity.class));
    }

    private void showRegistration() {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        editName.setVisibility(View.VISIBLE);
        editPhone.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        buttonFix.setVisibility(View.GONE);
        buttonFlexible.setVisibility(View.GONE);
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

    private void phoneVerificationStartPostRequest(final String phone, final String name) {
        WebMethods.getInstance().phoneVerificationStartPostRequest(DataController.getInstance().getSession().getAccessToken(), phone,
                new RequestListener<PhoneCodeResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        showSnackBar(R.string.phone_verification_error);
                    }

                    @Override
                    public void onRequestSuccess(PhoneCodeResponse phoneCodeResponse) {
                        startTimer();
                        if (phoneCodeResponse.getPhoneVerification() != null &&
                                phoneCodeResponse.getPhoneVerification().getCode() != null) {
                            Profile profile = new Profile();
                            profile.setPhone(phone);
                            profile.setFillName(name);
                            profile.setCode(phoneCodeResponse.getPhoneVerification().getCode());
                            profilePatchRequest(profile);
                        } else {
                            showSnackBar(R.string.code_write_second);
                            TransitionManager.beginDelayedTransition(getCoordinatorLayout());
                            editCode.setVisibility(View.VISIBLE);
                            buttonNext.setText("ПОДТВЕРДИТЬ");
                        }
                    }
                });
    }

    private void profilePatchRequest(Profile profile) {
        WebMethods.getInstance().profilePatchRequest(
                DataController.getInstance().getSession().getAccessToken(),
                profile.getProfileForPatchRequest(),
                new RequestListener<DefaultResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        showSnackBar(R.string.code_is_wrong);
                    }

                    @Override
                    public void onRequestSuccess(DefaultResponse defaultResponse) {
                        goToBuketActivity();
                    }
                }
        );
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(getCoordinatorLayout());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textResendCode.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 45000);
    }

}