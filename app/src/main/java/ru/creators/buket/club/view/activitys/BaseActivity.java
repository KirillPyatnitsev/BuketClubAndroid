package ru.creators.buket.club.view.activitys;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {

    protected View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected <T extends View> T getViewById(int id){
        return (T) findViewById(id);
    }

    protected abstract View assignRootView();

    protected View getRootView(){
        if (rootView==null){
            assignRootView();
        }

        return rootView;
    }

    protected void showSnackBar(int stringResId){
        Snackbar.make(getRootView(), getString(stringResId), Snackbar.LENGTH_LONG).show();
    }
}
