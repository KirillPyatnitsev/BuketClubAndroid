package ru.creators.buket.club.view.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ru.creators.buket.club.R;

public class DeliveryInfoFillingActivity extends BaseActivity {

    private ImageView imageBack;
    private ImageView imageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info_filling);

        assignView();
        assignListener();
        initView();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_dif_coordinator_root;
    }

    private void assignView(){
        imageBack = getViewById(R.id.i_ab_image_back);
        imageLogo = getViewById(R.id.i_ab_image_icon);
    }

    private void assignListener(){
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView(){
        imageBack.setVisibility(View.VISIBLE);
        imageLogo.setVisibility(View.INVISIBLE);
    }
}
