package ru.creators.buket.club;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

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
        WebMethods.getInstance().setSpiceManager(spiceManager);
        spiceManager.start(this);
    }



    @Override
    public void onTerminate() {
        spiceManager.shouldStop();
        super.onTerminate();
    }
}
