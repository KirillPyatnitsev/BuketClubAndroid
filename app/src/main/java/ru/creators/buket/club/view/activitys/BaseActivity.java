package ru.creators.buket.club.view.activitys;

import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.transitionseverywhere.TransitionManager;

import ru.creators.buket.club.R;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.web.WebMethods;

public abstract class BaseActivity extends AppCompatActivity {

    protected CoordinatorLayout coordinatorLayout;

    private SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    private RelativeLayout relativeLoading;
    private int runnigProcessCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebMethods.getInstance().setSpiceManager(spiceManager);
    }

    protected <T extends View> T getViewById(int id){
        return (T) findViewById(id);
    }

    private void assignRootView(){
        coordinatorLayout = getViewById(getCoordinatorViewId());
    }

    protected abstract int getCoordinatorViewId();

    protected CoordinatorLayout getCoordinatorLayout(){
        if (coordinatorLayout == null){
            assignRootView();
        }

        return coordinatorLayout;
    }

    protected void showSnackBar(int stringResId){
        Snackbar.make(getCoordinatorLayout(), getString(stringResId), Snackbar.LENGTH_LONG).show();
    }

    protected int getImageBlurId(){
        return 0;
    }

    protected int getContentContainerId(){
        return 0;
    }

    protected void showBlur(){
        if (getImageBlurId()!=0 && getContentContainerId()!=0){
            Bitmap blur = Helper.fastBlur(Helper.getBitmapFromView(coordinatorLayout), 20);
            ImageView imageViewBlur = getViewById(getImageBlurId());
            View viewForHide = getViewById(getContentContainerId());

            TransitionManager.beginDelayedTransition(coordinatorLayout);
            viewForHide.setVisibility(View.GONE);
            imageViewBlur.setImageBitmap(blur);
            imageViewBlur.setVisibility(View.VISIBLE);
        }
    }

    protected void hideBlur(){
        if (getImageBlurId()!=0 && getContentContainerId()!=0){
            ImageView imageViewBlur = getViewById(getImageBlurId());
            View viewForHide = getViewById(getContentContainerId());

            TransitionManager.beginDelayedTransition(coordinatorLayout);
            viewForHide.setVisibility(View.VISIBLE);
            imageViewBlur.setVisibility(View.GONE);

        }
    }

    private RelativeLayout getRelativeLoading(){
        if (relativeLoading == null){
            relativeLoading = getViewById(R.id.i_p_progress);
        }
        return relativeLoading;
    }

    protected void startLoading(){
        if (runnigProcessCount == 0){
            getRelativeLoading().setVisibility(View.VISIBLE);
        }
        runnigProcessCount++;
    }

    protected void stopLoading(){
        if (runnigProcessCount != 0) runnigProcessCount--;
        if (runnigProcessCount == 0){
            getRelativeLoading().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }
}
