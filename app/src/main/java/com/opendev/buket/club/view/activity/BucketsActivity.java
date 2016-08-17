package com.opendev.buket.club.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.tools.PreferenceCache;
import com.transitionseverywhere.TransitionManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import fr.ganfra.materialspinner.MaterialSpinner;
import com.opendev.buket.club.AppException;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ApplicationMode;
import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.Pagination;
import com.opendev.buket.club.model.PriceRange;
import com.opendev.buket.club.model.lists.ListBouquet;
import com.opendev.buket.club.model.lists.ListDictionaryItem;
import com.opendev.buket.club.model.lists.ListOrder;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.view.adapters.GridAdapterBouquet;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.BouquetsResponse;
import com.opendev.buket.club.web.response.DefaultResponse;
import com.opendev.buket.club.web.response.DictionaryResponse;
import com.opendev.buket.club.web.response.OrdersResponse;
import com.opendev.buket.club.web.response.PriceRangeResponse;

public class BucketsActivity extends BaseActivity {

    private static final String TAG = ServerConfig.TAG_PREFIX + "BucketsActivity";

    private ImageView imageOpenFilter;
    private ImageView imageCloseFilter;
    private ImageView imageOrders;
    private ImageView imageActionBarBackground;

    private TextView textActionBarTitle;
    private View viewActonBarFilter;

    private TextView textCurrentCostMin;
    private TextView textCurrentCostMax;

    private TextView textCostMix;
    private TextView textCostMax;

    private MaterialSpinner spinnerFlowerType;
    private MaterialSpinner spinnerFlowerColor;
    private MaterialSpinner spinnerDayEvent;

    private GridView gridView;
    private TextView textNotFindBouquets;

    private SwipeRefreshLayout swipeRefreshLayout;

    private GridAdapterBouquet gridAdapterBouquet;

    private RangeSeekBar rangeSeekBarCost;

    private final ListBouquet listBouquet = DataController.getInstance().getBouquetList();

    private final ListDictionaryItem dictionaryFlowerTypes = new ListDictionaryItem();
    private final ListDictionaryItem dictionaryFlowerColors = new ListDictionaryItem();
    private final ListDictionaryItem dictionaryDayEvents = new ListDictionaryItem();

    private ArrayAdapter<String> adapterFlowerTypes;
    private ArrayAdapter<String> adapterFlowerColors;
    private ArrayAdapter<String> adapterDayEvents;

    private int currentFlowerTypeId = -1;
    private int currentFlowerColorId = -1;
    private int currentDayEventId = -1;

    private int currentMinPrice = -1;
    private int currentMaxPrice = -1;

    private Pagination pagination;
    private int lastLoadedPage;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_buckets);

        assignView();
        assignListener();
        initView();

        loadPriceRange();
        loadDictionaries();
        listBouquetsGetResponse(true);

        if (DataController.getInstance().getSession().getAppMode() == ApplicationMode.COST_FLEXIBLE) {
            getOrders();
        }
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_b_coordinator_root;
    }

    private void assignView() {
        imageOpenFilter = getViewById(R.id.i_ab_image_settings_open);
        imageCloseFilter = getViewById(R.id.i_ab_image_settings_close);
        imageActionBarBackground = getViewById(R.id.a_b_image_action_bar_background);
        imageOrders = getViewById(R.id.i_ab_image_orders);
        rangeSeekBarCost = getViewById(R.id.a_b_seek_bar);

        textActionBarTitle = getViewById(R.id.a_b_text_action_bar_title);
        viewActonBarFilter = getViewById(R.id.a_b_view_filter);

        textCurrentCostMin = getViewById(R.id.a_b_text_current_cost_min);
        textCurrentCostMax = getViewById(R.id.a_b_text_current_cost_max);

        textCostMix = getViewById(R.id.a_b_text_cost_min);
        textCostMax = getViewById(R.id.a_b_text_cost_max);

        gridView = getViewById(R.id.a_b_grid_view_buckets);

        spinnerFlowerType = getViewById(R.id.i_bf_spinner_flowers_type);
        spinnerFlowerColor = getViewById(R.id.i_bf_spinner_tone_bouquet);
        spinnerDayEvent = getViewById(R.id.i_bf_spinner_chose_event);

        textNotFindBouquets = getViewById(R.id.a_b_text_not_find);

        swipeRefreshLayout = getViewById(R.id.a_b_swipe_refresh);
    }

    private void initView() {
        imageOpenFilter.setVisibility(View.VISIBLE);
        imageOrders.setVisibility(View.VISIBLE);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        gridAdapterBouquet = new GridAdapterBouquet(listBouquet, this, displaymetrics.widthPixels);
        gridView.setAdapter(gridAdapterBouquet);

        adapterFlowerTypes = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterFlowerColors = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterDayEvents = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);

        spinnerFlowerType.setAdapter(adapterFlowerTypes);
        spinnerFlowerColor.setAdapter(adapterFlowerColors);
        spinnerDayEvent.setAdapter(adapterDayEvents);
    }

    private void assignListener() {
        imageOpenFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilter();
            }
        });

        imageCloseFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter();
            }
        });

        rangeSeekBarCost.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                int min = (int) minValue;
                int max = (int) maxValue;
                textCurrentCostMin.setText(Helper.intToPriceString(min, BucketsActivity.this));
                textCurrentCostMax.setText(Helper.intToPriceString(max, BucketsActivity.this));
                if (currentMaxPrice != max || currentMinPrice != min) {
                    currentMaxPrice = max;
                    currentMinPrice = min;
                    listBouquetsGetResponse(true);
                }
            }
        });

        spinnerFlowerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFlowerTypeId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listBouquetsGetResponse(true);
            }
        });

        spinnerFlowerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFlowerColorId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDayEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentDayEventId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataController.getInstance().setBouquet(listBouquet.get(position));
                goToBouquetDetailsActivity();
            }
        });

        imageOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOrdersActivity();
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listBouquet.size() != 0 && (firstVisibleItem + visibleItemCount) == listBouquet.size()) {
                    listBouquetsGetResponse(false);
                }
            }
        });
    }

    private void goToOrdersActivity() {
        startActivity(new Intent(this, OrdersActivity.class));
    }

    private void goToBouquetDetailsActivity() {
        Helper.gotoActivity(this, BucketDetalisActivity.class);
    }

    private void openFilter() {
        toggleFilter(true);
    }

    private void closeFilter() {
        toggleFilter(false);
        listBouquetsGetResponse(true);
    }

    private void toggleFilter(boolean show) {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        if (show) {
            showBlur();
        } else {
            hideBlur();
        }
        final RelativeLayout.LayoutParams layout =
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        final int margin = show? R.dimen.margin_top_action_bar_a_b_open
                : R.dimen.margin_top_action_bar_a_b_close;
        layout.setMargins(
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                getResources().getDimensionPixelOffset(margin),
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                0);

        imageActionBarBackground.setLayoutParams(layout);
        forViews(textActionBarTitle, imageOpenFilter).setVisibility(show ? View.GONE : View.VISIBLE);
        forViews(viewActonBarFilter, imageCloseFilter).setVisibility(show ? View.VISIBLE: View.GONE);
    }

    private void setSpinnerData(int min, int max) {
        rangeSeekBarCost.setRangeValues(min, max);
        rangeSeekBarCost.setSelectedMinValue(min);
        rangeSeekBarCost.setSelectedMaxValue(max);

        textCostMix.setText(Helper.intToPriceString(min, this));
        textCostMax.setText(Helper.intToPriceString(max, this));

        textCurrentCostMin.setText(Helper.intToPriceString(min, this));
        textCurrentCostMax.setText(Helper.intToPriceString(max, this));
    }

    @Override
    protected int getContentContainerId() {
        return R.id.a_b_relative_content_container;
    }

    @Override
    protected int getImageBlurId() {
        return R.id.a_b_blur_image;
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
                    adapter.clear();
                    String[] itemsForAdapter = loadedItems.getValuesArray();
                    adapter.addAll(itemsForAdapter);
                }
                stopLoading();
            }
        });
    }

    private void loadDictionaries() {
        getDictionary(Rest.FLOWER_TYPES, dictionaryFlowerTypes, adapterFlowerTypes);
        getDictionary(Rest.FLOWER_COLORS, dictionaryFlowerColors, adapterFlowerColors);
        getDictionary(Rest.DAY_EVENTS, dictionaryDayEvents, adapterDayEvents);
    }

    private void listBouquetsGetResponse(boolean reloadList) {
        if (reloadList) {
            lastLoadedPage = 0;
            pagination = null;
            listBouquet.clear();
        }

        if (pagination == null || lastLoadedPage < pagination.getNextPage()) {
            lastLoadedPage = pagination == null ? 1 : pagination.getNextPage();
            updateListBouquet(lastLoadedPage);
        }
    }

    private void updateListBouquet(int page) {
        if (!swipeRefreshLayout.isRefreshing()) {
            startLoading();
        }
        WebMethods.getInstance().loadBouquets(
                currentFlowerTypeId == -1 ? currentFlowerTypeId : dictionaryFlowerTypes.getItemId(currentFlowerTypeId),
                currentFlowerColorId == -1 ? currentFlowerColorId : dictionaryFlowerColors.getItemId(currentFlowerColorId),
                currentDayEventId == -1 ? currentDayEventId : dictionaryDayEvents.getItemId(currentDayEventId),
                currentMinPrice,
                currentMaxPrice, page, Pagination.PER_PAGE,
                new RequestListener<BouquetsResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        if (!swipeRefreshLayout.isRefreshing()) {
                            stopLoading();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onRequestSuccess(BouquetsResponse bouquetsResponse) {
                        pagination = bouquetsResponse.getMeta().getPagination();
                        listBouquet.addAll(bouquetsResponse.getListBouquet());

                        textNotFindBouquets.setVisibility(listBouquet.isEmpty() ?
                                View.VISIBLE : View.GONE);

                        Log.d(TAG, "device_token: " + PreferenceCache.getString(getApplicationContext(), PreferenceCache.KEY_GCM_TOKEN));

                        gridAdapterBouquet.notifyDataSetChanged();

                        if (!swipeRefreshLayout.isRefreshing()) {
                            stopLoading();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void getOrders() {
        startLoading();
        WebMethods.getInstance().getOrders(1, 100,
                new RequestListener<OrdersResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(OrdersResponse ordersResponse) {
                        stopLoading();
                        if (ordersResponse.getListOrder() != null && ordersResponse.getListOrder().size() != 0) {
                            removeOrders(ordersResponse.getListOrder());
                        }
                    }
                });
    }

    private void removeOrders(ListOrder listOrder) {
        for (Order order : listOrder) {
            if (order.isFillingShop()) {
                removeOrderRequest(order.getId());
            }
        }
    }

    private void removeOrderRequest(int orderId) {
        startLoading();
        WebMethods.getInstance().removeOrderRequest(orderId, new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                stopLoading();
            }
        });
    }

    private void loadPriceRange() {
        PriceRange range = DataController.getInstance().getPriceRange();
        if (range == null) {
            startLoading();
            WebMethods.getInstance().loadPriceRange(new RequestListener<PriceRangeResponse>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    stopLoading();
                }

                @Override
                public void onRequestSuccess(PriceRangeResponse priceRangeResponse) {
                    PriceRange loadedRange = priceRangeResponse.getPriceRange();
                    if (loadedRange == null) {
                        Log.e(TAG, "Failed to load price range");
                    } else {
                        showPriceRange(loadedRange);
                    }
                    stopLoading();
                }
            });
        } else {
            showPriceRange(range);
        }
    }

    private void showPriceRange(PriceRange priceRange) {
        setSpinnerData(priceRange.getMinPrice(), priceRange.getMaxPrice());
        DataController.getInstance().setPriceRange(priceRange);
    }
}