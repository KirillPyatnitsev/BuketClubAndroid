package com.opendev.buket.club.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.AppException;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ApplicationMode;
import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Bouquet;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.Pagination;
import com.opendev.buket.club.model.PriceRange;
import com.opendev.buket.club.model.lists.ListBouquet;
import com.opendev.buket.club.model.lists.ListDictionaryItem;
import com.opendev.buket.club.model.lists.ListOrder;
import com.opendev.buket.club.tools.MyGridView;
import com.opendev.buket.club.tools.PreferenceCache;
import com.opendev.buket.club.view.adapters.GridAdapterBouquet;
import com.opendev.buket.club.view.adapters.ListAdapterBouquet;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.BouquetsResponse;
import com.opendev.buket.club.web.response.DefaultResponse;
import com.opendev.buket.club.web.response.DictionaryResponse;
import com.opendev.buket.club.web.response.OrdersResponse;
import com.opendev.buket.club.web.response.PriceRangeResponse;
import com.transitionseverywhere.TransitionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BucketsActivity extends BaseActivity {

    private static final String TAG = ServerConfig.TAG_PREFIX + "BucketsActivity";

//    private ImageView imageOpenFilter;
//    private ImageView imageCloseFilter;
//    private ImageView imageOrders;
//    private ImageView imageActionBarBackground;
//
//    private TextView textActionBarTitle;
//    private View viewActonBarFilter;
//
//    private TextView textCurrentCostMin;
//    private TextView textCurrentCostMax;
//
//    private TextView textCostMix;
//    private TextView textCostMax;

//    private MaterialSpinner spinnerFlowerType;
//  private MaterialSpinner spinnerFlowerColor;
//    private MaterialSpinner spinnerDayEvent;

   // private ListView listView;
    private MyGridView gridView;
  //  private RecyclerView recyclerView;
    private TextView textNotFindBouquets;

    //private SwipeRefreshLayout swipeRefreshLayout;

    private ListAdapterBouquet listAdapterBouquet;
    private GridAdapterBouquet gridAdapterBouquet;

    DisplayMetrics displaymetrics;

//    private RangeSeekBar rangeSeekBarCost;

    private final ListBouquet listBouquet = DataController.getInstance().getBouquetList();

    private final ListDictionaryItem dictionaryFlowerTypes = new ListDictionaryItem();
    private final ListDictionaryItem dictionaryFlowerColors = new ListDictionaryItem();
    private final ListDictionaryItem dictionaryDayEvents = new ListDictionaryItem();

    private ArrayAdapter<String> adapterFlowerTypes;
    private ArrayAdapter<String> adapterFlowerColors;
    private ArrayAdapter<String> adapterDayEvents;

    private RelativeLayout mSizeLayout;
    private RelativeLayout lSizeLayout;
    private RelativeLayout xlSizeLayout;

    private TextView mSizeText;
    private TextView lSizeText;
    private TextView xlSizeText;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Context context;
    private Toolbar toolbar;

    private Menu menu;

    private int currentFlowerTypeId = -1;
    private int currentFlowerColorId = -1;
    private int currentDayEventId = -1;

    private int currentMinPrice = -1;
    private int currentMaxPrice = -1;

    private Pagination pagination;
    private int lastLoadedPage;
    private int size;
    private int state;

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
//        imageOpenFilter = getViewById(R.id.i_ab_image_settings_open);
//        imageCloseFilter = getViewById(R.id.i_ab_image_settings_close);
//        imageActionBarBackground = getViewById(R.id.a_b_image_action_bar_background);
//        imageOrders = getViewById(R.id.i_ab_image_orders);
//        rangeSeekBarCost = getViewById(R.id.a_b_seek_bar);
//
//        textActionBarTitle = getViewById(R.id.a_b_text_action_bar_title);
//        viewActonBarFilter = getViewById(R.id.a_b_view_filter);
//
//        textCurrentCostMin = getViewById(R.id.a_b_text_current_cost_min);
//        textCurrentCostMax = getViewById(R.id.a_b_text_current_cost_max);
//
//        textCostMix = getViewById(R.id.a_b_text_cost_min);
//        textCostMax = getViewById(R.id.a_b_text_cost_max);

        toolbar = getViewById(R.id.bouquets_toolbar);
      //  listView = getViewById(R.id.a_b_list_view_buckets);
        gridView = getViewById(R.id.a_b_grid_view_buckets);

        mSizeText = getViewById(R.id.m_size_text);
        lSizeText = getViewById(R.id.l_size_text);
        xlSizeText = getViewById(R.id.xl_size_text);

        mSizeLayout = getViewById(R.id.m_size_layout);
        lSizeLayout = getViewById(R.id.l_size_layout);
        xlSizeLayout = getViewById(R.id.xl_size_layout);

      //  spinnerFlowerType = getViewById(R.id.i_bf_spinner_flowers_type);
     //   spinnerFlowerColor = getViewById(R.id.i_bf_spinner_tone_bouquet);
      //  spinnerDayEvent = getViewById(R.id.i_bf_spinner_chose_event);

        textNotFindBouquets = getViewById(R.id.a_b_text_not_find);

        swipeRefreshLayout = getViewById(R.id.a_b_swipe_refresh);
    }

    private void initView() {
//        imageOpenFilter.setVisibility(View.VISIBLE);
//        imageOrders.setVisibility(View.VISIBLE);


        state = DataController.getInstance().getCatalogueState();
        context = this;
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        listAdapterBouquet = new ListAdapterBouquet(listBouquet, this, Bouquet.SIZE_MEDIUM, displaymetrics.widthPixels);
//        listView.setAdapter(listAdapterBouquet);


        gridAdapterBouquet = new GridAdapterBouquet(listBouquet, this, Bouquet.SIZE_MEDIUM, displaymetrics.widthPixels);


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.list_bouquet_footer, null);
        if (state == DataController.CATALOGUE_LIST) {
            gridView.setNumColumns(1);
            gridView.setAdapter(listAdapterBouquet);
        } else {

            gridView.setNumColumns(2);
            gridView.setAdapter(gridAdapterBouquet);
            gridView.invalidateViews();
        }
//        recyclerView.setHasFixedSize(true);

//        GridLayoutManager manager = new GridLayoutManager(this, 2);

//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(gridAdapterBouquet);
        lSizeText.setSelected(true);
        size = Bouquet.SIZE_MEDIUM;



        swipeRefreshLayout.setColorSchemeColors(ContextCompat.
                getColor(this, R.color.list_bouquet_color), ContextCompat.
                getColor(this, R.color.list_bouquet_color),ContextCompat.
                getColor(this, R.color.list_bouquet_color));

        setSupportActionBar(toolbar);

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Каталог");

        adapterFlowerTypes = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterFlowerColors = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);
        adapterDayEvents = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text);

        adapterFlowerTypes.setDropDownViewResource(R.layout.list_item_spiner2);
        adapterFlowerColors.setDropDownViewResource(R.layout.list_item_spiner2);
        adapterDayEvents.setDropDownViewResource(R.layout.list_item_spiner2);





//        spinnerFlowerType.setAdapter(adapterFlowerTypes);
//        spinnerFlowerColor.setAdapter(adapterFlowerColors);
//        spinnerDayEvent.setAdapter(adapterDayEvents);
    }

    private void assignListener() {

        mSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSizeText.setSelected(true);
                lSizeText.setSelected(false);
                xlSizeText.setSelected(false);
                size = Bouquet.SIZE_LITTLE;
                if (gridView.getNumColumns() == 1) {
                    listAdapterBouquet = new ListAdapterBouquet(listBouquet, context,
                            Bouquet.SIZE_LITTLE, displaymetrics.widthPixels);

                    gridView.setAdapter(listAdapterBouquet);
                    listAdapterBouquet.notifyDataSetChanged();
                } else {
                    gridAdapterBouquet = new GridAdapterBouquet(listBouquet, context,
                            Bouquet.SIZE_LITTLE, displaymetrics.widthPixels);

                    gridView.setAdapter(gridAdapterBouquet);
                    gridAdapterBouquet.notifyDataSetChanged();
                }
            }
        });

        lSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSizeText.setSelected(false);
                lSizeText.setSelected(true);
                xlSizeText.setSelected(false);
                size = Bouquet.SIZE_MEDIUM;
                if (gridView.getNumColumns() == 1) {
                    listAdapterBouquet = new ListAdapterBouquet(listBouquet, context,
                            Bouquet.SIZE_MEDIUM, displaymetrics.widthPixels);

                    gridView.setAdapter(listAdapterBouquet);
                    listAdapterBouquet.notifyDataSetChanged();
                } else {
                    gridAdapterBouquet = new GridAdapterBouquet(listBouquet, context,
                            Bouquet.SIZE_MEDIUM, displaymetrics.widthPixels);

                    gridView.setAdapter(gridAdapterBouquet);
                    gridAdapterBouquet.notifyDataSetChanged();
                }
            }
        });

        xlSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSizeText.setSelected(false);
                lSizeText.setSelected(false);
                xlSizeText.setSelected(true);
                size = Bouquet.SIZE_GREAT;
                if (gridView.getNumColumns() == 1) {
                    listAdapterBouquet = new ListAdapterBouquet(listBouquet, context,
                            Bouquet.SIZE_GREAT, displaymetrics.widthPixels);

                    gridView.setAdapter(listAdapterBouquet);
                    listAdapterBouquet.notifyDataSetChanged();
                } else {
                    gridAdapterBouquet = new GridAdapterBouquet(listBouquet, context,
                            Bouquet.SIZE_GREAT, displaymetrics.widthPixels);

                    gridView.setAdapter(gridAdapterBouquet);
                    gridAdapterBouquet.notifyDataSetChanged();
                }
            }
        });
//        imageOpenFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFilter();
//            }
//        });
//
//        imageCloseFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeFilter();
//            }
//        });

//        rangeSeekBarCost.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
//            @Override
//            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
//                int min = (int) minValue;
//                int max = (int) maxValue;
//                textCurrentCostMin.setText(Helper.intToPriceString(min, BucketsActivity.this));
//                textCurrentCostMax.setText(Helper.intToPriceString(max, BucketsActivity.this));
//                if (currentMaxPrice != max || currentMinPrice != min) {
//                    currentMaxPrice = max;
//                    currentMinPrice = min;
//                    listBouquetsGetResponse(true);
//                }
//            }
//        });

//        spinnerFlowerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                currentFlowerTypeId = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listBouquetsGetResponse(true);
            }
        });

//        spinnerFlowerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                currentFlowerColorId = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        spinnerDayEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                currentDayEventId = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataController.getInstance().setBouquet(listBouquet.get(position));
                DataController.getInstance().setSize(size);
                if (gridView.getNumColumns() == 1) {
                    DataController.getInstance().setCatalogueState(DataController.CATALOGUE_LIST);
                } else {
                    DataController.getInstance().setCatalogueState(DataController.CATALOGUE_GRID);
                }
                goToBouquetDetailsActivity();
            }
        });

//        imageOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToOrdersActivity();
//            }
//        });

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

       /* gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listBouquet.size() != 0 && (firstVisibleItem + visibleItemCount) == listBouquet.size()) {
                    listBouquetsGetResponse(false);
                }
            }
        });*/
    }

    private void goToOrdersActivity() {
        startActivity(new Intent(this, OrdersActivity.class));
    }

    private void goToFilterActivity() {
        startActivityForResult(new Intent(this, FilterActivity.class), 1);
    }

    private void goToBouquetDetailsActivity() {
        startActivity(new Intent(this, BucketDetalisActivity.class));
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

//        imageActionBarBackground.setLayoutParams(layout);
//        forViews(textActionBarTitle, imageOpenFilter).setVisibility(show ? View.GONE : View.VISIBLE);
//        forViews(viewActonBarFilter, imageCloseFilter).setVisibility(show ? View.VISIBLE: View.GONE);
    }

//    private void setSpinnerData(int min, int max) {
//        rangeSeekBarCost.setRangeValues(min, max);
//        rangeSeekBarCost.setSelectedMinValue(min);
//        rangeSeekBarCost.setSelectedMaxValue(max);
//
//        textCostMix.setText(Helper.intToPriceString(min, this));
//        textCostMax.setText(Helper.intToPriceString(max, this));
//
//        textCurrentCostMin.setText(Helper.intToPriceString(min, this));
//        textCurrentCostMax.setText(Helper.intToPriceString(max, this));
//    }

//    @Override
//    protected int getContentContainerId() {
//        return R.id.a_b_relative_content_container;
//    }

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

                        listAdapterBouquet.notifyDataSetChanged();
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
                    //    showPriceRange(loadedRange);
                    }
                    stopLoading();
                }
            });
        } else {
         //   showPriceRange(range);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        currentFlowerTypeId = data.getIntExtra("type", -1);
        currentFlowerColorId = data.getIntExtra("color", -1);
        currentDayEventId = data.getIntExtra("event", -1);
        listBouquetsGetResponse(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalogue, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    private void stopScroll(AbsListView view)
    {
        try
        {
            Field field = android.widget.AbsListView.class.getDeclaredField("mFlingRunnable");
            field.setAccessible(true);
            Object flingRunnable = field.get(view);
            if (flingRunnable != null)
            {
                Method method = Class.forName("android.widget.AbsListView$FlingRunnable").getDeclaredMethod("endFling");
                method.setAccessible(true);
                method.invoke(flingRunnable);
            }
        }
        catch (Exception e) {}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_catalogue:
                if (gridView.getNumColumns() == 1) {
                    stopScroll(gridView);
                    gridView.setAdapter(gridAdapterBouquet);
                    gridView.setNumColumns(2);
                    MenuItem menuItem = menu.findItem(R.id.action_catalogue);
                    menuItem.setIcon(R.drawable.ic_action_catalog2);
                    return true;
                } else {
                    stopScroll(gridView);
                    gridView.setAdapter(listAdapterBouquet);
                    gridView.setNumColumns(1);
                    gridView.invalidateViews();
                    MenuItem menuItem = menu.findItem(R.id.action_catalogue);
                    menuItem.setIcon(R.drawable.ic_catalogue);
                    return true;
                }
            case R.id.action_bullets:
                goToOrdersActivity();
                return true;
            case R.id.action_filter:
                goToFilterActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


    }

    //    private void showPriceRange(PriceRange priceRange) {
//        setSpinnerData(priceRange.getMinPrice(), priceRange.getMaxPrice());
//        DataController.getInstance().setPriceRange(priceRange);
//    }
}
