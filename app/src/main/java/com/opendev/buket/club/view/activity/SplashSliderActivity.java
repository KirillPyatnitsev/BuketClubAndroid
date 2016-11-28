package com.opendev.buket.club.view.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.gcm.QuickstartPreferences;
import com.opendev.buket.club.gcm.RegistrationIntentService;
import com.opendev.buket.club.model.Profile;
import com.opendev.buket.club.model.Session;
import com.opendev.buket.club.tools.PreferenceCache;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.DefaultResponse;
import com.opendev.buket.club.web.response.ProfileResponse;
import com.opendev.buket.club.web.response.SessionResponse;

public class SplashSliderActivity extends BaseActivity {

    private Button buttonRegister;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = ServerConfig.TAG_PREFIX + "SplashScreen";

    private ViewPager viewPager;
    private int[] colors;
    private int[] layouts;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArgbEvaluator argbEvaluator;
    private Context context;
    private TextView noThanksTextView;
    ValueAnimator mColorAnimation;


    private Session session;
    private Profile profile;
    private boolean isLoading = false;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_splash_slider);


        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean tokenSent = sharedPreferences
                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

        if (checkPlayServices() && !tokenSent) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }



        makeTransparentBar();

        assignView();
        assignListener();
        assignLayouts();

        addBottomDots(0);

        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        initColors();

        mColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colors[0], colors[1], colors[2]);
        mColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                viewPager.setBackgroundColor((Integer)animator.getAnimatedValue());
            }

        });
        mColorAnimation.setDuration((3 - 1) * 10000000000l);
        context = this;
        argbEvaluator = new ArgbEvaluator();


        if(!tokenSent) {
            startLoading();
            isLoading = true;
        }
        if(tokenSent) {
            startApp();
        }





        Log.d(TAG, "deviceId: " + getUniqueDeviceId());

        assignView();


        assignListener();


        LocalBroadcastManager.getInstance(this).registerReceiver(gcmRegistrationDone,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    private BroadcastReceiver gcmRegistrationDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isLoading) {
                stopLoading();
            }
            startApp();

        }
    };

    private void createSession() {
        startLoading();
        Log.d("debugg", "startCreateSession()");
        PreferenceCache.removeKey(this, PreferenceCache.KEY_SESSION);
        WebMethods.getInstance().createSession(getUniqueDeviceId(),
                PreferenceCache.getString(this, PreferenceCache.KEY_GCM_TOKEN), ServerConfig.BUKET_CLUB_PROJECT_ID,
                new RequestListener<SessionResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(SessionResponse sessionResponse) {
                        Session session = sessionResponse.getSession();
                        saveSession(session);
                        SplashSliderActivity.this.session = session;
                        getProfile();
                        Log.d("debugg", "CreateSession()");
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
            }
        }
    }

    private void saveSession(Session session) {
        DataController.getInstance().setSession(session);
    }

    private void goToBuketActivity() {
        startActivity(new Intent(this, BucketsActivity.class));
    }

    private void startApp() {
        session = PreferenceCache.getObject(this, PreferenceCache.KEY_SESSION, Session.class);
        if (session == null) {
            createSession();
        } else {
            getProfile();
        }
    }


    private void generateTypePrice() {
        Log.d("debugg", "startgenerate()");
        startLoading();
        WebMethods.getInstance().generateTypePrice(new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                getProfile();

                Log.d("debugg", "GenerateTypePrice()");
                stopLoading();
            }
        });
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gcmRegistrationDone);
        super.onPause();
    }
    private void getProfile() {
        startLoading();
        Log.d("debugg", "startgetprofile()");
        WebMethods.getInstance().getProfile(new RequestListener<ProfileResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                createSession();
                Log.d("debugg", "getProfileErr()");
                stopLoading();
            }

            @Override
            public void onRequestSuccess(ProfileResponse profileResponse) {
                Profile profile = profileResponse.getProfile();
                SplashSliderActivity.this.profile = profile;
                if(profile == null) {
                    Crashlytics.log("profile == null");
                } else {
                    if (profile.getTypePriceIndex() != Profile.TYPE_PRICE_FIX) {
                        generateTypePrice();
                    }
                }

                Log.d("debugg", "getProfileSucc()");
                stopLoading();
                if (session != null && profile != null && profile.getPhone() != null && !profile.getPhone().isEmpty()) {
                    findViewById(getCoordinatorViewId()).setVisibility(View.GONE);
                }
            }
        });
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

    private void initColors(){
        colors = new int[3];
        colors[0] = ContextCompat.getColor(this, R.color.slider1_color);
        colors[1] = ContextCompat.getColor(this, R.color.slider2_color);
        colors[2] = ContextCompat.getColor(this, R.color.slider3_color);


    }



    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_sslider_coordinator_root;
    }

    private void assignView() {
        //buttonRegister = (Button) findViewById(R.id.slide1_reg_button);
        viewPager = (ViewPager) findViewById(R.id.splash_view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        buttonRegister = (Button) findViewById(R.id.slide_reg_button);
        noThanksTextView = (TextView) findViewById(R.id.slide_no_thanks);

    }

    private void assignLayouts() {
        layouts = new int[]{
                R.layout.splash_slide1,
                R.layout.splash_slide2,
                R.layout.splash_slide3
        };
    }

    private void assignListener() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSplashActivity();
            }
        });

        noThanksTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void goToSplashActivity(){
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void makeTransparentBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];


        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226; "));
            dots[i].setTextSize(35);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.color_dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.color_dot_active));
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mColorAnimation.setCurrentPlayTime((long)((positionOffset + position)* 10000000000l));
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);




            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private String getUniqueDeviceId() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id;
    }
}


