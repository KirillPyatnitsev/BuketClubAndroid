package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.transitionseverywhere.TransitionManager;

import ru.creators.buket.club.R;

public class PaymentTypeActivity extends BaseActivity {

    public static final int PAY_CASH = 0;
    public static final int PAY_CARD = 1;
    public static final int PAY_W1 = 2;

    private int currentPayType = PAY_CARD;

    private ImageView imageBack;

    private ImageView imagePayCash;
    private ImageView imagePayCard;
    private ImageView imagePayW1;

    private LinearLayout linearPayCash;
    private LinearLayout linearPayCard;
    private LinearLayout linearPayW1;

    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        assignView();
        assignListener();
        initView();
    }

    private void assignView() {
        imageBack = getViewById(R.id.i_ab_image_back);

        imagePayCash = getViewById(R.id.a_pt_image_pay_cash);
        imagePayCard = getViewById(R.id.a_pt_image_pay_card);
        imagePayW1 = getViewById(R.id.a_pt_image_pay_w1);

        linearPayCash = getViewById(R.id.a_pt_linear_pay_cash);
        linearPayCard = getViewById(R.id.a_pt_linear_pay_card);
        linearPayW1 = getViewById(R.id.a_pt_linear_pay_w1);

        buttonNext = getViewById(R.id.a_pt_button_next);
    }

    private void assignListener() {
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

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextAct();
            }
        });
    }

    private void initView() {
        getImagePay(currentPayType).setBackgroundResource(R.drawable.round_action_bar);
        imageBack.setVisibility(View.VISIBLE);
    }

    private void setPayType(int payType) {
        if (payType != currentPayType) {
            TransitionManager.beginDelayedTransition(getCoordinatorLayout());
            getImagePay(payType).setBackgroundResource(R.drawable.round_action_bar);
            getImagePay(currentPayType).setBackgroundResource(R.drawable.round_pay_type_unselected);
            currentPayType = payType;
        }
    }

    private ImageView getImagePay(int payType) {
        switch (payType) {
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

    private void goToNextAct() {
        switch (currentPayType) {
            case PAY_CARD:
                startActivity(PayDoneActivity.class);
                break;
            case PAY_CASH:
                startActivity(PayDoneActivity.class);
                break;
            case PAY_W1:
                startActivity(PayDoneActivity.class);
                break;

        }
    }

    private void startActivity(Class<?> c) {
        startActivity(new Intent(this, c));
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pt_coordinator_root;
    }
}
