package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import ru.creators.buket.club.R;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startActivity(new Intent(this, BucketsActivity.class));
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_ss_relative_root;
    }
}