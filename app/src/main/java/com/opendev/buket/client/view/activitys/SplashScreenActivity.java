package com.opendev.buket.client.view.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.transitionseverywhere.TransitionManager;

import com.opendev.buket.client.DataController;
import com.opendev.buket.client.R;
import com.opendev.buket.client.consts.ServerConfig;
import com.opendev.buket.client.gcm.QuickstartPreferences;
import com.opendev.buket.client.gcm.RegistrationIntentService;
import com.opendev.buket.client.model.Profile;
import com.opendev.buket.client.model.Session;
import com.opendev.buket.client.tools.Helper;
import com.opendev.buket.client.tools.PreferenceCache;
import com.opendev.buket.client.view.custom.maskededittext.MaskedEditText;
import com.opendev.buket.client.web.WebMethods;
import com.opendev.buket.client.web.response.DefaultResponse;
import com.opendev.buket.client.web.response.PhoneCodeResponse;
import com.opendev.buket.client.web.response.ProfileResponse;
import com.opendev.buket.client.web.response.SessionResponse;

public class SplashScreenActivity extends BaseActivity {

    private static final boolean TEST_APPLICATION_MODE = false;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = ServerConfig.TAG_PREFIX + "SplashScreen";

    private Session session;
    private Profile profile;

    private Button buttonFlexible;
    private Button buttonFix;
    private Button buttonNext;
    private TextView textResendCode;

    private EditText editName;
    private EditText editCode;
    private MaskedEditText editPhone;

    private String phone;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean tokenSent = sharedPreferences
                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

        if (checkPlayServices() && !tokenSent) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        Log.d(TAG, "deviceId: " + getUniqueDeviceId());

        assignView();
        assignListener();

        if (TEST_APPLICATION_MODE) {
            buttonFix.setVisibility(View.VISIBLE);
            buttonFlexible.setVisibility(View.VISIBLE);
        } else {
            if (tokenSent) {
                startApp();
            }
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
            if (!TEST_APPLICATION_MODE) {
                startApp();
            }
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gcmRegistrationDone);
        super.onPause();
    }

    private void queueShowResend() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(getCoordinatorLayout());
                textResendCode.setVisibility(View.VISIBLE);
            }
        }, 45000);
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
        startLoading();
        PreferenceCache.removeKey(this, PreferenceCache.KEY_SESSION);
        WebMethods.getInstance().createSession(getUniqueDeviceId(),
                PreferenceCache.getString(this, PreferenceCache.KEY_GCM_TOKEN),
                new RequestListener<SessionResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(SessionResponse sessionResponse) {
                        Session session = sessionResponse.getSession();
                        saveSession(session);
                        SplashScreenActivity.this.session = session;
                        getProfile();
                        stopLoading();
                    }
                });
    }

    private void assignListener() {
        buttonFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp();
            }
        });

        buttonFlexible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp();
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
            } else if (Helper.isPhoneNumberValid(phone)) {
                this.phone = phone;
                phoneVerificationStartPostRequest(phone, name);
            } else {
                showSnackBar(R.string.phone_not_entered);
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

    private void startApp() {
        session = PreferenceCache.getObject(this, PreferenceCache.KEY_SESSION, Session.class);
        if (session == null) {
            createSession();
        } else {
            getProfile();
        }
    }

    private void saveSession(Session session) {
        DataController.getInstance().setSession(session);
    }

    private void getProfile() {
        startLoading();
        WebMethods.getInstance().getProfile(new RequestListener<ProfileResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                createSession();
                stopLoading();
            }

            @Override
            public void onRequestSuccess(ProfileResponse profileResponse) {
                Profile profile = profileResponse.getProfile();
                SplashScreenActivity.this.profile = profile;
                if(profile == null) {
                    Crashlytics.log("profile == null");
                } else {
                    if (profile.getTypePriceIndex() != Profile.TYPE_PRICE_FIX) {
                        generateTypePrice();
                    }
                }
                stopLoading();
            }
        });
    }

    private void generateTypePrice() {
        startLoading();
        WebMethods.getInstance().generateTypePrice(new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                getProfile();
                stopLoading();
            }
        });
    }

    @Override
    protected void allProcessDone() {
        if (session != null && profile != null) {
            session.setAppMode(profile.getTypePriceIndex());
            saveSession(session);
            DataController.getInstance().setSession(session);
            DataController.getInstance().setProfile(profile);

            if (profile.getPhone() != null && !profile.getPhone().isEmpty()) {
                goToBuketActivity();
            } else {
                showRegistration();
            }
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
        WebMethods.getInstance().phoneVerificationStartPostRequest(phone,
                new RequestListener<PhoneCodeResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        showSnackBar(R.string.phone_verification_error);
                    }

                    @Override
                    public void onRequestSuccess(PhoneCodeResponse phoneCodeResponse) {
                        queueShowResend();
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
        WebMethods.getInstance().profilePatchRequest(profile.getProfileForPatchRequest(),
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

    private String getUniqueDeviceId() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id;
    }
}