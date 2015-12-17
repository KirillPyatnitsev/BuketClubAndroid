package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Timer;
import java.util.TimerTask;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.OrderResponse;

public class PayDoneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_done);

        sendOrder();
    }

    private void startClosingTimer(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {

                goToBouquetsActivity();

                // If you want to call Activity then call from here for 5 seconds it automatically call and your image disappear....
            }
        }, 5000);
    }

    private void goToBouquetsActivity(){
        startActivity(new Intent(this, BucketsActivity.class));
    }

    private void sendOrder(){
        startLoading(false);

        WebMethods.getInstance().sendOrder(DataController.getInstance().getSession().getAccessToken(),
                DataController.getInstance().getOrder(),
                new RequestListener<OrderResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        showSnackBar("Ошибка создания заказа");
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(OrderResponse orderResponse) {
                        showSnackBar("Заказ создан");
                        startClosingTimer();
                        stopLoading();
                    }
                });
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pd_coordinator_root;
    }
}