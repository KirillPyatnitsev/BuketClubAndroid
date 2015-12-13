package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.model.Session;

public class SplashScreenActivity extends BaseActivity {

    private Session session;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_ss_relative_root;
    }
}