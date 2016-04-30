package ru.creators.buket.club.view.activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
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

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.web.WebMethods;

public abstract class BaseActivity extends AppCompatActivity {

    protected CoordinatorLayout coordinatorLayout;

//    private SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    private RelativeLayout relativeLoading;
    private ImageView imageLoadingBlur;
    private int runnigProcessCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataController.getInstance().setBaseActivity(this);
//        WebMethods.getInstance().setSpiceManager(spiceManager);
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

    public void showSnackBar(int stringResId){
        showSnackBar(getString(stringResId));
    }

    public void showSnackBar(String message){
        Snackbar.make(getCoordinatorLayout(), message, Snackbar.LENGTH_LONG).show();
    }

    protected int getImageBlurId(){
        return 0;
    }

    protected int getContentContainerId(){
        return 0;
    }

    protected void showBlur(){
//        if (getImageBlurId()!=0 && getContentContainerId()!=0){
//            Bitmap blur = Helper.fastBlur(Helper.getBitmapFromView(coordinatorLayout), 20);
//            ImageView imageViewBlur = getViewById(getImageBlurId());
//            View viewForHide = getViewById(getContentContainerId());
//
//            TransitionManager.beginDelayedTransition(coordinatorLayout);
//            viewForHide.setVisibility(View.GONE);
//            imageViewBlur.setImageBitmap(blur);
//            imageViewBlur.setVisibility(View.VISIBLE);
//        }
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

    private ImageView getImageLoadingBlur(){
        if (imageLoadingBlur == null){
            imageLoadingBlur = getViewById(R.id.i_p_image_blur);
        }
        return imageLoadingBlur;
    }

    protected void startLoading(){
        startLoading(false);
    }


    protected void startLoading(boolean showBlur){
        showBlur = false;
        if (runnigProcessCount == 0){
            getRelativeLoading().setVisibility(View.VISIBLE);
            if (showBlur) getImageLoadingBlur().setImageBitmap(Helper.fastBlur(Helper.getBitmapFromView(coordinatorLayout), 20));
        }
        runnigProcessCount++;
    }

    protected boolean noRunnigProcess(){
        return runnigProcessCount == 0;
    }

    public void stopLoading(){
        if (runnigProcessCount != 0) runnigProcessCount--;
        if (runnigProcessCount == 0){
            getRelativeLoading().setVisibility(View.GONE);
            allProcessDone();
        }
    }

    protected void allProcessDone(){

    }

    protected String getUniqueDeviceId(){
        return Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataController.getInstance().setAppIsFolded(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        DataController.getInstance().setAppIsFolded(true);
        super.onPause();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
