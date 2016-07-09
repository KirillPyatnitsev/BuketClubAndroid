package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.flurry.android.FlurryAgent;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Timer;
import java.util.TimerTask;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.DefaultResponse;

public class PayDoneActivity extends BaseActivity {

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_pay_done);

        sendOrder();
        if (DataController.getInstance().getOrder().getTypePaymentIndex() == Order.TYPE_PAYMENT_INDEX_CARD) {
            getViewById(R.id.a_pd_text_action_bar_title).setVisibility(View.VISIBLE);
        }
    }

    private void startClosingTimer() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                goToOrderDetails();
                // If you want to call Activity then call from here for 5 seconds it automatically call and your image disappear....
            }
        }, 5000);
    }

    private void goToOrderDetails() {
        startActivity(new Intent(this, OrderDetailsActivity.class));
    }

    private void sendOrder() {
        startLoading(false);

        RequestListener<DefaultResponse> listenerPath = new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
//                showSnackBar("Ошибка создания заказа");
                stopLoading();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
//                showSnackBar("Заказ создан");
                stopLoading();
                startClosingTimer();
            }
        };

        WebMethods.getInstance().orderPathRequest(DataController.getInstance().getSession().getAccessToken(),
                DataController.getInstance().getOrder().getOrderForServer(),
                DataController.getInstance().getOrder().getId(), listenerPath);

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pd_coordinator_root;
    }
}
