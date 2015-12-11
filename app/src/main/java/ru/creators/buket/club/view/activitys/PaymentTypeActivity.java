package ru.creators.buket.club.view.activitys;

import android.media.Image;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.transitionseverywhere.TransitionManager;

import ru.creators.buket.club.R;

public class PaymentTypeActivity extends BaseActivity {

    private final int PAY_CASH = 0;
    private final int PAY_CARD = 1;
    private final int PAY_W1 = 2;

    private int currentPayType = PAY_CARD;

    private ImageView imageBack;

    private ImageView imagePayCash;
    private ImageView imagePayCard;
    private ImageView imagePayW1;

    private LinearLayout linearPayCash;
    private LinearLayout linearPayCard;
    private LinearLayout linearPayW1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        assignView();
        assignListener();
        initView();
    }

    private void assignView(){
        imageBack = getViewById(R.id.i_ab_image_back);

        imagePayCash = getViewById(R.id.a_pt_image_pay_cash);
        imagePayCard = getViewById(R.id.a_pt_image_pay_card);
        imagePayW1 = getViewById(R.id.a_pt_image_pay_w1);

        linearPayCash = getViewById(R.id.a_pt_linear_pay_cash);
        linearPayCard = getViewById(R.id.a_pt_linear_pay_card);
        linearPayW1 = getViewById(R.id.a_pt_linear_pay_w1);
    }

    private void assignListener(){
        linearPayCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayType(PAY_CASH);
            }
        });

        linearPayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayType(PAY_CARD);
            }
        });

        linearPayW1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPayType(PAY_W1);
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView(){
        getImagePay(currentPayType).setBackgroundResource(R.drawable.round_action_bar);
        imageBack.setVisibility(View.VISIBLE);
    }

    private void setPayType(int payType){
        if (payType != currentPayType){
            TransitionManager.beginDelayedTransition(getCoordinatorLayout());
            getImagePay(payType).setBackgroundResource(R.drawable.round_action_bar);
            getImagePay(currentPayType).setBackgroundResource(R.drawable.round_pay_type_unselected);
            currentPayType = payType;
        }
    }

    private ImageView getImagePay(int payType){
        switch (payType){
            case PAY_CARD:
                return imagePayCard;
            case PAY_CASH:
                return imagePayCash;
            case PAY_W1:
                return imagePayW1;
            default:
                return imagePayCash;
        }
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pt_coordinator_root;
    }
}
