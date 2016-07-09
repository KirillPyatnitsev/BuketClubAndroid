package ru.creators.buket.club.view.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.transitionseverywhere.TransitionManager;
import com.yandex.money.api.methods.params.P2pTransferParams;
import com.yandex.money.api.methods.params.PaymentParams;

import java.math.BigDecimal;

import ru.creators.buket.club.BuildConfig;
import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.DefaultResponse;
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
    protected void onCreateInternal() {
        setContentView(R.layout.activity_payment_type);

        assignView();
        assignListener();
        initView();

        if (BuildConfig.DEBUG) {
            yandexPaymentOk();
        } else {
            payYandexMoney();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_YA_MONEY) {
            if (resultCode == RESULT_OK) {
                yandexPaymentOk();
            } else {
                finish();
            }
        }
    }

    private void yandexPaymentOk() {
        DataController.getInstance().getOrder().setTypePayment(Order.TYPE_PAYMENT_CARD);
        DataController.getInstance().getOrder().setTypePaymentIndex(Order.TYPE_PAYMENT_INDEX_CARD);
        startActivity(new Intent(this, PayDoneActivity.class));
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
                payYandexMoney();
                break;
            case PAY_CASH:
                DataController.getInstance().getOrder().setTypePayment(Order.TYPE_PAYMENT_CASH);
                DataController.getInstance().getOrder().setTypePaymentIndex(Order.TYPE_PAYMENT_INDEX_CASH);
                startActivity(new Intent(this, PayDoneActivity.class));
                break;
            case PAY_W1:
                startActivity(new Intent(this, PayDoneActivity.class));
                break;

        }
    }

    private void payYandexMoney() {
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

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeOrderRequest(order.getId());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setMessage(R.string.remove_order_dialog_text);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removeOrderRequest(int orderId) {
        startLoading();
        WebMethods.getInstance().removeOrderRequest(DataController.getInstance().getSession().getAccessToken(), orderId, new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
                goToBouquetsActivity();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                stopLoading();
                goToBouquetsActivity();
            }
        });
    }

    private void goToBouquetsActivity() {
        startActivity(new Intent(this, BucketsActivity.class));
    }
}
