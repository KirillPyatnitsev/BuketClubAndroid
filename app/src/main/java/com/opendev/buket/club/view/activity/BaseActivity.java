package com.opendev.buket.club.view.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = ServerConfig.TAG_PREFIX + "BaseActivity";
    private CoordinatorLayout coordinatorLayout;

    private RelativeLayout relativeLoading;
    private int runningProcessCount = 0;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: " + this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);

        DataController.getInstance().setBaseActivity(this);

        this.onCreateInternal();

        String className = this.getClass().getSimpleName();
        FlurryAgent.logEvent(className);
    }

    @Override
    protected final void onDestroy() {
        DataController.getInstance().removeBaseActivity(this);
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + this.getClass().getName());
    }

    protected abstract void onCreateInternal();

    protected final <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    private void assignRootView() {
        coordinatorLayout = getViewById(getCoordinatorViewId());
    }

    protected abstract int getCoordinatorViewId();

    protected final CoordinatorLayout getCoordinatorLayout() {
        if (coordinatorLayout == null) {
            assignRootView();
        }
        return coordinatorLayout;
    }

    public final void showSnackBar(int stringResId) {
        showSnackBar(getString(stringResId));
    }

    public final void showSnackBar(String message) {
        Snackbar.make(getCoordinatorLayout(), message, Snackbar.LENGTH_LONG).show();
    }

    protected int getImageBlurId() {
        return 0;
    }

    protected int getContentContainerId() {
        return 0;
    }

    protected final void showBlur() {
    }

    protected final void hideBlur() {
    }

    private RelativeLayout getRelativeLoading() {
        if (relativeLoading == null) {
            relativeLoading = getViewById(R.id.i_p_progress);
        }
        return relativeLoading;
    }

    protected final void startLoading() {
        if (runningProcessCount == 0) {
            getRelativeLoading().setVisibility(View.VISIBLE);
        }
        runningProcessCount++;
    }

    public final void stopLoading() {
        if (runningProcessCount != 0) {
            runningProcessCount--;
        }
        if (runningProcessCount == 0) {
            getRelativeLoading().setVisibility(View.GONE);
            allProcessDone();
        }
    }

    protected void allProcessDone() {
        // Override to perform actions when all requests are done
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataController.getInstance().setAppIsFolded(false);
    }

    @Override
    protected void onPause() {
        DataController.getInstance().setAppIsFolded(true);
        super.onPause();
    }

    public final boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void pushMessageReceived(String message) {
        showSnackBar(message);
    }

    protected int getWindowWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    protected final ViewComposite forViews(View... views) {
        return new ViewComposite(views);
    }

    /**
     * Convenience class to perform operations on group of views.
     */
    public static class ViewComposite {
        private List<View> views = new ArrayList<>();

        public ViewComposite(View... views) {
            if (views != null) {
                Collections.addAll(this.views, views);
            }
        }

        public void setVisibility(final int visibility) {
            forEach(new ViewAction() {
                @Override
                public void run(View v) {
                    v.setVisibility(visibility);
                }
            });
        }

        public void forEach(ViewAction action) {
            for (View v : views) {
                action.run(v);
            }
        }
    }

    public interface ViewAction {
        void run(View v);
    }
}
