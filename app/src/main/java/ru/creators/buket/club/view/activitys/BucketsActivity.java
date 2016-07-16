package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.transitionseverywhere.TransitionManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import fr.ganfra.materialspinner.MaterialSpinner;
import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.consts.ApplicationMode;
import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.Pagination;
import ru.creators.buket.club.model.PriceRange;
import ru.creators.buket.club.model.lists.ListBouquet;
import ru.creators.buket.club.model.lists.ListDictionaryItem;
import ru.creators.buket.club.model.lists.ListOrder;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.view.adapters.GridAdapterBouquet;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.BouquetsResponse;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.DictionaryResponse;
import ru.creators.buket.club.web.response.OrdersResponse;
import ru.creators.buket.club.web.response.PriceRangeResponse;

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

    private PriceRange priceRange = DataController.getInstance().getPriceRange();
    private ListBouquet listBouquet = DataController.getInstance().getListBouquet();

    private ListDictionaryItem dictionaryFlowerTypes = new ListDictionaryItem();
    private ListDictionaryItem dictionaryFlowerColors = new ListDictionaryItem();
    private ListDictionaryItem dictionaryDayEvents = new ListDictionaryItem();

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

        loadPriceRange();

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
                textCurrentCostMin.setText(Helper.intToPriceString((int) minValue));
                textCurrentCostMax.setText(Helper.intToPriceString((int) maxValue));
                if (currentMaxPrice != (int) maxValue || currentMinPrice != (int) minValue) {
                    currentMaxPrice = (int) maxValue;
                    currentMinPrice = (int) minValue;
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
        startActivity(new Intent(this, BucketDetalisActivity.class));
    }

    private void initView() {
        if (priceRange != null && listBouquet != null) {
            imageOpenFilter.setVisibility(View.VISIBLE);
            imageOrders.setVisibility(View.VISIBLE);
            setSpinnerData(priceRange.getMinPrice(), priceRange.getMaxPrice());

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            gridAdapterBouquet = new GridAdapterBouquet(listBouquet, this, displaymetrics.widthPixels);
            gridView.setAdapter(gridAdapterBouquet);

            loadDictionaries();
        }
    }

    private void openFilter() {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        showBlur();
        RelativeLayout.LayoutParams backgroundImageLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundImageLayoutParams.setMargins(
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                getResources().getDimensionPixelOffset(R.dimen.margin_top_action_bar_a_b_open),
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                0
        );

        imageActionBarBackground.setLayoutParams(backgroundImageLayoutParams);

        textActionBarTitle.setVisibility(View.GONE);
        viewActonBarFilter.setVisibility(View.VISIBLE);

        imageOpenFilter.setVisibility(View.GONE);
        imageCloseFilter.setVisibility(View.VISIBLE);
    }

    private void closeFilter() {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        hideBlur();
        RelativeLayout.LayoutParams backgroundImageLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundImageLayoutParams.setMargins(
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                getResources().getDimensionPixelOffset(R.dimen.margin_top_action_bar_a_b_close),
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                0
        );

        imageActionBarBackground.setLayoutParams(backgroundImageLayoutParams);

        textActionBarTitle.setVisibility(View.VISIBLE);
        viewActonBarFilter.setVisibility(View.GONE);

        imageOpenFilter.setVisibility(View.VISIBLE);
        imageCloseFilter.setVisibility(View.GONE);

        listBouquetsGetResponse(true);
    }

    private void setSpinnerData(int min, int max) {
        rangeSeekBarCost.setRangeValues(min, max);
        rangeSeekBarCost.setSelectedMinValue(min);
        rangeSeekBarCost.setSelectedMaxValue(max);

        textCostMix.setText(Helper.intToPriceString(min));
        textCostMax.setText(Helper.intToPriceString(max));

        textCurrentCostMin.setText(Helper.intToPriceString(min));
        textCurrentCostMax.setText(Helper.intToPriceString(max));
    }

    @Override
    protected int getContentContainerId() {
        return R.id.a_b_relative_content_container;
    }

    @Override
    protected int getImageBlurId() {
        return R.id.a_b_blur_image;
    }

    private void getDictionary(final String dictionaryType) {
        startLoading(false);
        WebMethods.getInstance().getDictionary(dictionaryType, new RequestListener<DictionaryResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(DictionaryResponse dictionaryResponse) {
                if (dictionaryType.equals(Rest.FLOWER_TYPES)) {
                    dictionaryFlowerTypes.clear();
                    dictionaryFlowerTypes.addAll(dictionaryResponse.getDictonaryFlowerTypes());
                } else if (dictionaryType.equals(Rest.FLOWER_COLORS)) {
                    dictionaryFlowerColors.clear();
                    dictionaryFlowerColors.addAll(dictionaryResponse.getDictonaryFloverClors());
                } else if (dictionaryType.equals(Rest.DAY_EVENTS)) {
                    dictionaryDayEvents.clear();
                    dictionaryDayEvents.addAll(dictionaryResponse.getDictonaryDayEvents());
                }
                stopLoading();
            }
        });
    }

    private void loadDictionaries() {
        getDictionary(Rest.FLOWER_TYPES);
        getDictionary(Rest.FLOWER_COLORS);
        getDictionary(Rest.DAY_EVENTS);
    }

    @Override
    protected void allProcessDone() {
        if (dictionaryFlowerTypes.size() != 0 && adapterFlowerTypes == null) {
            adapterFlowerTypes = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text, dictionaryFlowerTypes.getValuesArray());
            spinnerFlowerType.setAdapter(adapterFlowerTypes);
        }

        if (dictionaryFlowerColors.size() != 0 && adapterFlowerColors == null) {
            adapterFlowerColors = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text, dictionaryFlowerColors.getValuesArray());
            spinnerFlowerColor.setAdapter(adapterFlowerColors);
        }

        if (dictionaryDayEvents.size() != 0 && adapterDayEvents == null) {
            adapterDayEvents = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text, dictionaryDayEvents.getValuesArray());
            spinnerDayEvent.setAdapter(adapterDayEvents);
        }
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
            startLoading(false);
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

                        if (listBouquet.isEmpty()) {
                            textNotFindBouquets.setVisibility(View.VISIBLE);
                        } else {
                            textNotFindBouquets.setVisibility(View.GONE);
                        }

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
        startLoading(false);
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
        startLoading(false);
        WebMethods.getInstance().loadPriceRange(new RequestListener<PriceRangeResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
            }

            @Override
            public void onRequestSuccess(PriceRangeResponse priceRangeResponse) {
                priceRange = priceRangeResponse.getPriceRange();
                initView();
                stopLoading();
            }
        });
    }

}
