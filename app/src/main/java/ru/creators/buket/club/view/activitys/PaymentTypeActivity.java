package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.transitionseverywhere.TransitionManager;
import com.yandex.money.api.methods.params.P2pTransferParams;
import com.yandex.money.api.methods.params.PaymentParams;

import java.math.BigDecimal;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.yandex.money.android.PaymentActivity;

public class PaymentTypeActivity extends BaseActivity {

    private static final String CLIENT_ID = "CF81271080DB5D1AC2C1659FA16962AAD09FCEEBF3D3DF88DF32B7FD243EE77D";
    private static final String P2P = "410013897372739";
    private static final int REQUEST_CODE_YA_MONEY = 1488;

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

    private Order order = DataController.getInstance().getOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        assignView();
        assignListener();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_YA_MONEY && resultCode == RESULT_OK) {
            startActivity(new Intent(this, PayDoneActivity.class));
        }
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
                pay();
                break;
            case PAY_CASH:
//                DataController.getInstance().getOrder().set
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

    private void pay(){
//        String telephone = editPhone.getText().toString().replaceAll("[^\\d.]", "");
        PaymentParams phoneParams = new P2pTransferParams.Builder(P2P)
                .setAmount(new BigDecimal(order.getPrice()))
                .setMessage("Оплата за " + order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()) + ". Android.")
                .create();
        Intent intent = PaymentActivity.getBuilder(this)
                .setPaymentParams(phoneParams)
                .setClientId(CLIENT_ID)
                .build();
        startActivityForResult(intent, REQUEST_CODE_YA_MONEY);
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pt_coordinator_root;
    }
}
