package ru.creators.buket.club;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

import io.fabric.sdk.android.Fabric;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.web.WebMethods;

/**
 * Created by mifkamaz on 09/12/15.
 */
public class BuketClubApplication  extends Application {

    private SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        WebMethods.getInstance().setSpiceManager(spiceManager);
        spiceManager.start(this);

        // configure Flurry
        FlurryAgent.setLogEnabled(false);

        // init Flurry
        FlurryAgent.init(this, ServerConfig.FLURRY_API_KEY);
    }



    @Override
    public void onTerminate() {
        spiceManager.shouldStop();
        DataController.getInstance().setBaseActivity(null);
        super.onTerminate();
    }
}
