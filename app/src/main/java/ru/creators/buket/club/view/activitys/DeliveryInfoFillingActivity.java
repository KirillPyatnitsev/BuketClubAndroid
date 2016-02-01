package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;

import org.codehaus.jackson.map.util.ISO8601Utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;
import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Profile;

public class DeliveryInfoFillingActivity extends BaseActivity {

    private ImageView imageBack;
    private ImageView imageLogo;

    private MaterialSpinner spinnerDeliveryTime;
    private EditText editRecipientName;
    private EditText editPhoneNumber;
    private EditText editComment;
    private PlacesAutocompleteTextView editAddress;

    private Button buttonNext;

    private Date currentDate;

    private ArrayList<String> deliveryTime = new ArrayList<>();

    private ArrayAdapter<String> delivetyTimeAdapter;

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean reseliction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info_filling);

        assignView();
        initView();
        assignListener();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_dif_coordinator_root;
    }

    private void assignView() {
        imageBack = getViewById(R.id.i_ab_image_back);
        imageLogo = getViewById(R.id.i_ab_image_icon);

        spinnerDeliveryTime = getViewById(R.id.a_dif_spinner_delivery_type);
        editRecipientName = getViewById(R.id.a_dif_edit_recipient);
        editPhoneNumber = getViewById(R.id.a_dif_edit_phone);
        editComment = getViewById(R.id.a_dif_edit_comment);
        editAddress = getViewById(R.id.a_dif_edit_delivery_address);

        buttonNext = getViewById(R.id.a_dif_button_next);
    }

    private void assignListener() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addDataToOrder()){
                    goToNextActivity();
                }else{
                    showSnackBar(getString(R.string.delivery_info_error));
                }
            }
        });

        spinnerDeliveryTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    currentDate = null;
                } else {
                    if (!reseliction) {
                        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                                .setListener(slideDateTimeListener)
                                .setInitialDate(new Date())
                                .setIs24HourTime(true)
                                .setMinDate(new Date())
                                .setIndicatorColor(getResources().getColor(R.color.yellow))
                                .build()
                                .show();
                    } else {
                        reseliction = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        imageBack.setVisibility(View.VISIBLE);
        imageLogo.setVisibility(View.INVISIBLE);

        deliveryTime.add(getString(R.string.text_time_soon));

        if (DataController.getInstance().getOrder().getTimeDelivery() != null) {
            currentDate = ISO8601Utils.parse(DataController.getInstance().getOrder().getTimeDelivery());
            deliveryTime.add(formatter.format(currentDate));
        } else {
            currentDate = null;
            deliveryTime.add("Выбрать время");
        }

//        editAddress.setText(DataController.getInstance().getOrder().getAddress());

        delivetyTimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deliveryTime);

        spinnerDeliveryTime.setAdapter(delivetyTimeAdapter);

        spinnerDeliveryTime.setSelection(currentDate == null ? 0 : 1);

    }

    private SlideDateTimeListener slideDateTimeListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            reseliction = true;
            currentDate = date;
            deliveryTime.set(1, formatter.format(currentDate));
            spinnerDeliveryTime.setAdapter(delivetyTimeAdapter);
            spinnerDeliveryTime.setSelection(1);
        }

        @Override
        public void onDateTimeCancel() {

        }
    };

    private boolean addDataToOrder() {
        DataController.getInstance().getOrder().setRecipientPhone(editPhoneNumber.getText().toString());
        DataController.getInstance().getOrder().setRecipientName(editRecipientName.getText().toString());
        DataController.getInstance().getOrder().setAddress(editAddress.getText().toString());
        DataController.getInstance().getOrder().setComment(editComment.getText().toString());
        DataController.getInstance().getOrder().setTimeDelivery(
                currentDate != null ? ISO8601Utils.format(currentDate) : null
        );

        return (!editPhoneNumber.getText().toString().isEmpty()
                && editPhoneNumber.getText().toString().replaceAll("[^\\d.]", "").length() == 11
                && !editRecipientName.getText().toString().isEmpty()
                && !editAddress.getText().toString().isEmpty());

    }

    private void goToNextActivity() {
        switch (DataController.getInstance().getSession().getAppMode()) {
            case Profile.TYPE_PRICE_FIX:
                startActivity(new Intent(this, PaymentTypeActivity.class));
                break;
            case Profile.TYPE_PRICE_FLEXIBLE:
                startActivity(new Intent(this, ChoseShopActivity.class));
                break;
        }
    }
}
