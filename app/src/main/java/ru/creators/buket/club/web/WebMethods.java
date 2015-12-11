package ru.creators.buket.club.web;

import android.widget.ImageView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class WebMethods {
    private static WebMethods mWebMethods = new WebMethods();

    public static WebMethods getInstance() {
        return mWebMethods;
    }

    public void setSpiceManager(SpiceManager mSpiceManager) {
        this.mSpiceManager = mSpiceManager;
    }

    public SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    public WebMethods() {}

    public WebMethods(SpiceManager mSpiceManager) {
        this.mSpiceManager = mSpiceManager;
    }

    private SpiceManager mSpiceManager;

    private RetryPolicy getRetryPolicy() {
        return new RetryPolicy() {
            @Override
            public int getRetryCount() {
                return 0;
            }

            @Override
            public void retry(SpiceException e) {

            }

            @Override
            public long getDelayBeforeRetry() {
                return 0;
            }
        };
    }


}
