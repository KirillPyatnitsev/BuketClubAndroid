package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.yandex.money.api.methods.params.P2pTransferParams;
import com.yandex.money.api.methods.params.PaymentParams;
import com.yandex.money.api.methods.params.PhoneParams;

import java.math.BigDecimal;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.view.custom.maskededittext.MaskedEditText;
import ru.yandex.money.android.PaymentActivity;

public class CardInfoFillingActivity extends BaseActivity {

    private Order order = DataController.getInstance().getOrder();

    private static final String CLIENT_ID = "CF81271080DB5D1AC2C1659FA16962AAD09FCEEBF3D3DF88DF32B7FD243EE77D";
    private static final String P2P = "410013897372739";
    private static final int REQUEST_CODE_YA_MONEY = 1488;

    private MaskedEditText editPhone;
    private TextView textBouquetName;
    private TextView textBouquetCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info_filling);

        assignView();
        assignListener();
        initView();
        FlurryAgent.logEvent("CardInfoFillingActivity");
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_cif_coordinator_root;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_YA_MONEY && resultCode == RESULT_OK) {
            startActivity(new Intent(this, PayDoneActivity.class));
        }
    }

    private void assignView(){
        editPhone = getViewById(R.id.a_cif_edit_phone);
        textBouquetName = getViewById(R.id.a_cif_text_action_bar_second);
        textBouquetCost = getViewById(R.id.a_cif_text_action_bar_title);
    }

    private void assignListener(){
        getViewById(R.id.a_cif_button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
    }

    private void initView(){
        textBouquetCost.setText(Helper.getStringWithCostPrefix(order.getPrice(), this));
        textBouquetName.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));
    }

    private void pay(){
//        String telephone = editPhone.getText().toString().replaceAll("[^\\d.]", "");
        PaymentParams phoneParams = new P2pTransferParams.Builder(P2P)
                .setAmount(new BigDecimal(order.getPrice()))
                .create();
        Intent intent = PaymentActivity.getBuilder(this)
                .setPaymentParams(phoneParams)
                .setClientId(CLIENT_ID)
                .build();
        startActivityForResult(intent, REQUEST_CODE_YA_MONEY);
    }

}
