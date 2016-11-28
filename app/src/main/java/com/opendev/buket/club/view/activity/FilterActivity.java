package com.opendev.buket.club.view.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.AppException;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.model.lists.ListDictionaryItem;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.DictionaryResponse;


public class FilterActivity extends BaseActivity {
    private final ListDictionaryItem dictionaryFlowerTypes = new ListDictionaryItem();
    private final ListDictionaryItem dictionaryFlowerColors = new ListDictionaryItem();
    private final ListDictionaryItem dictionaryDayEvents = new ListDictionaryItem();

    private ArrayAdapter<String> adapterFlowerTypes;
    private ArrayAdapter<String> adapterFlowerColors;
    private ArrayAdapter<String> adapterDayEvents;

    private Spinner typeSpinner;
    private Spinner colorSpinner;
    private Spinner eventSpinner;

    private int currentFlowerTypeId = 0;
    private int currentFlowerColorId = 0;
    private int currentDayEventId = 0;

    private Button applyButton;

    private TextView cancelFilterText;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_bouquet_filter);

        assignView();
        assignListener();
        initView();

        loadDictionaries();


    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.filter_coord_layout;
    }

    private void assignView(){
        typeSpinner = getViewById(R.id.filter_type_spinner);
        colorSpinner = getViewById(R.id.filter_color_spinner);
        eventSpinner = getViewById(R.id.filter_event_spinner);

        applyButton = getViewById(R.id.apply_filter_button);

        cancelFilterText = getViewById(R.id.cancel_filter_text);
    }

    private void assignListener(){
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentFlowerColorId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentFlowerTypeId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentDayEventId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("type", currentFlowerTypeId);
                intent.putExtra("color", currentFlowerColorId);
                intent.putExtra("event", currentDayEventId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelFilterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFlowerTypeId = -1;
                currentFlowerColorId = -1;
                currentDayEventId = -1;
                Intent intent = new Intent();
                intent.putExtra("type", currentFlowerTypeId);
                intent.putExtra("color", currentFlowerColorId);
                intent.putExtra("event", currentDayEventId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initView(){


       // adapterFlowerTypes = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterFlowerTypes = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterFlowerTypes.setDropDownViewResource(R.layout.list_item_spiner2);
      //  adapterFlowerColors = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterFlowerColors = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterFlowerColors.setDropDownViewResource(R.layout.list_item_spiner2);
      //  adapterDayEvents = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterDayEvents = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterDayEvents.setDropDownViewResource(R.layout.list_item_spiner2);

        typeSpinner.setAdapter(adapterFlowerTypes);
        colorSpinner.setAdapter(adapterFlowerColors);
        eventSpinner.setAdapter(adapterDayEvents);

    }

    private void loadDictionaries(){
        getDictionary(Rest.FLOWER_TYPES, dictionaryFlowerTypes, adapterFlowerTypes);
        getDictionary(Rest.FLOWER_COLORS, dictionaryFlowerColors, adapterFlowerColors);
        getDictionary(Rest.DAY_EVENTS, dictionaryDayEvents, adapterDayEvents);
    }

    private void getDictionary(final String dictionaryType,
                               final ListDictionaryItem list,
                               final ArrayAdapter<String> adapter) {
        startLoading();
        WebMethods.getInstance().getDictionary(dictionaryType, new RequestListener<DictionaryResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(DictionaryResponse dictionaryResponse) {
                ListDictionaryItem loadedItems;
                if (dictionaryType.equals(Rest.FLOWER_TYPES)) {
                    loadedItems = dictionaryResponse.getDictonaryFlowerTypes();
                } else if (dictionaryType.equals(Rest.FLOWER_COLORS)) {
                    loadedItems = dictionaryResponse.getDictonaryFloverClors();
                } else if (dictionaryType.equals(Rest.DAY_EVENTS)) {
                    loadedItems = dictionaryResponse.getDictonaryDayEvents();
                } else {
                    throw new AppException("Unknown dictionary received: " + dictionaryType);
                }
                if (!loadedItems.isEmpty()) {
                    list.clear();
                    list.addAll(loadedItems);
                    String[] itemsForAdapter = loadedItems.getValuesArray();
                    adapter.addAll(itemsForAdapter);
                    adapter.notifyDataSetChanged();
                }
                stopLoading();
            }
        });
    }

    class SpinAdapter extends BaseAdapter implements SpinnerAdapter {

        private Context context;
        private String[] arr;

        public SpinAdapter(Context context) {
            this.context = context;
            arr = new String[3];
        }

        public String[] getArr() {
            return arr;
        }

        public void setArr(String[] arr) {
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int i) {
            return arr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if(convertView == null){

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_spiner2, viewGroup, false);


            }

            ((TextView) convertView.findViewById(R.id.li_s_text)).setText(arr[i]);

            return convertView;
        }
    }
}


