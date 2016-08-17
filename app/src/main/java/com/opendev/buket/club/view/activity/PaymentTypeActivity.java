package com.opendev.buket.club.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.consts.ServerConfig;
import com.yandex.money.api.methods.params.P2pTransferParams;
import com.yandex.money.api.methods.params.PaymentParams;

import java.math.BigDecimal;

import com.opendev.buket.club.BuildConfig;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.DefaultResponse;
import ru.yandex.money.android.PaymentActivity;

public class PaymentTypeActivity extends BaseActivity {

    private static final int REQUEST_CODE_YA_MONEY = 1488;

    private ImageView imageBack;
    private TextView textViewConfirmPayment;
    private Button buttonTryAgainPay;

    private Order order = DataController.getInstance().getOrder();

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_payment_type);

        assignView();
        initView();

        if (BuildConfig.DEBUG) {
//            payYandexMoney();
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
                textViewConfirmPayment.setText(getString(R.string.failed_payment));
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
        textViewConfirmPayment = getViewById(R.id.a_pt_text_confirm_payment);
        buttonTryAgainPay = getViewById(R.id.a_pt_button_try_pay_again);
    }

    private void initView() {
        imageBack.setVisibility(View.VISIBLE);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonTryAgainPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payYandexMoney();
            }
        });
    }

    private void payYandexMoney() {

        BigDecimal price;
        if (BuildConfig.DEBUG) {
            price = new BigDecimal(1);
        } else {
            price = new BigDecimal(order.getPrice());
        }

        PaymentParams cardParams = new P2pTransferParams.Builder(ServerConfig.YANDEX_KASSA_P2P)
                .setAmount(price)
                .setMessage("Оплата за заказ №" + order.getId() + ": " + order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()) + ". Android.")
                .create();
        Intent intent = PaymentActivity.getBuilder(this)
                .setPaymentParams(cardParams)
                .setClientId(ServerConfig.YANDEX_KASSA_CLIENT_ID)
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
        WebMethods.getInstance().removeOrderRequest(orderId, new RequestListener<DefaultResponse>() {
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
