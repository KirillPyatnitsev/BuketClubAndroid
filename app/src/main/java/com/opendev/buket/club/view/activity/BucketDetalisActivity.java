package com.opendev.buket.club.view.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.model.Bouquet;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.view.adapters.BouquetViewPagerAdapter;

public class BucketDetalisActivity extends BaseActivity {



//
//    private TextView textBouquetSizeLittle;
//    private TextView textBouquetSizeMedium;
//    private TextView textBouquetSizeGreat;
//
//    private ImageView imageBouquetSizeLittleBig;
//    private ImageView imageBouquetSizeMediumBig;
//    private ImageView imageBouquetSizeGreatBig;
//
//    private ImageView imageBouquetSizeLittleMin;
//    private ImageView imageBouquetSizeMediumMin;
//    private ImageView imageBouquetSizeGreatMin;
//
//    private RelativeLayout relativeBouquetSizeLittle;
//    private RelativeLayout relativeBouquetSizeMedium;
//    private RelativeLayout relativeBouquetSizeGreat;
//
//    private SizeViewHolder little;
//    private SizeViewHolder medium;
//    private SizeViewHolder great;
//
//    private ImageView imageBack;
//
//    private ImageView imageBouquet;
//    private TextView textPrice;
//    private TextView textBouquetName;
//

    private static final String BUY_STRING_BEFORE = "Купить за: ";
    private static final String BUY_STRING_AFTER = " руб.";
    private static final int CENTER_ITEM_NUMBER = 0;

    private int currentSize = Bouquet.SIZE_MEDIUM;

    private ViewPager bouquetViewPager;

    private TextView bouquetTitleText;
    private TextView bouquetCompText;
    private TextView bouquetRatingText;

    private RelativeLayout mSizeLayout;
    private RelativeLayout lSizeLayout;
    private RelativeLayout xlSizeLayout;

    private TextView bouquetDescText;

    private Bouquet bouquet;
    private int imageSize;

    private TextView mSizeText;
    private TextView lSizeText;
    private TextView xlSizeText;

    private TextView bouquetRatingSmallText;

    private Button buyButton;

    private Toolbar toolbar;
    private RelativeLayout sizeLayout;

    private String[] urls;
    private BouquetViewPagerAdapter bouquetViewPagerAdapter;

    private TextView[] dots;
    private LinearLayout dotsLayout;


    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_bucket_detalis);
        bouquet = DataController.getInstance().getBouquet();
        if (bouquet != null) {
            assignView();
            initView();
            assignListener();

//            imageBack.setVisibility(View.VISIBLE);
        } else {
            showSnackBar(R.string.oops_error);
            startActivity(new Intent(this, BucketsActivity.class));
        }
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_bd_coordinator_root;
    }

    private void assignView() {

        bouquetViewPager = getViewById(R.id.details_image);


        bouquetTitleText = getViewById(R.id.details_bouquet_title);
        bouquetCompText = getViewById(R.id.details_comp);

        bouquetRatingText = getViewById(R.id.details_bar_text);

        bouquetDescText = getViewById(R.id.details_desc);

        mSizeText = getViewById(R.id.m_size_text);
        lSizeText = getViewById(R.id.l_size_text);
        xlSizeText = getViewById(R.id.xl_size_text);

        buyButton = getViewById(R.id.details_buy_button);

        toolbar = getViewById(R.id.details_toolbar);

        sizeLayout = getViewById(R.id.details_size_layout);
        bouquetRatingSmallText = getViewById(R.id.rating_text_small);

        dotsLayout = (LinearLayout) findViewById(R.id.details_dots);


        mSizeLayout = getViewById(R.id.m_size_layout);
        lSizeLayout = getViewById(R.id.l_size_layout);
        xlSizeLayout = getViewById(R.id.xl_size_layout);


//        buttonBuy = getViewById(R.id.a_bd_button_bay);
//
//        textBouquetSizeLittle = getViewById(R.id.a_bd_text_bouquet_size_little);
//        imageBouquetSizeLittleBig = getViewById(R.id.a_bd_image_big_bouquet_size_little);
//        imageBouquetSizeLittleMin = getViewById(R.id.a_bd_image_min_bouquet_size_little);
//        relativeBouquetSizeLittle = getViewById(R.id.a_bd_relative_bouquet_little);
//
//        textBouquetSizeMedium = getViewById(R.id.a_bd_text_bouquet_size_medium);
//        imageBouquetSizeMediumBig = getViewById(R.id.a_bd_image_big_bouquet_size_medium);
//        imageBouquetSizeMediumMin = getViewById(R.id.a_bd_image_min_bouquet_size_medium);
//        relativeBouquetSizeMedium = getViewById(R.id.a_bd_relative_bouquet_medium);
//
//        textBouquetSizeGreat = getViewById(R.id.a_bd_text_bouquet_size_great);
//        imageBouquetSizeGreatBig = getViewById(R.id.a_bd_image_big_bouquet_size_great);
//        imageBouquetSizeGreatMin = getViewById(R.id.a_bd_image_min_bouquet_size_great);
//        relativeBouquetSizeGreat = getViewById(R.id.a_bd_relative_bouquet_great);
//
//        textBouquetName = getViewById(R.id.a_ab_text_bouquet_name);
//
//        textPrice = getViewById(R.id.a_ab_text_cost);
//        imageBouquet = getViewById(R.id.a_bd_image_bouquet);
//
//        little = new SizeViewHolder(textBouquetSizeLittle, imageBouquetSizeLittleBig, imageBouquetSizeLittleMin, relativeBouquetSizeLittle);
//        medium = new SizeViewHolder(textBouquetSizeMedium, imageBouquetSizeMediumBig, imageBouquetSizeMediumMin, relativeBouquetSizeMedium);
//        great = new SizeViewHolder(textBouquetSizeGreat, imageBouquetSizeGreatBig, imageBouquetSizeGreatMin, relativeBouquetSizeGreat);
//
//        little.setSelection(SizeViewHolder.UNSELECTED);
//        medium.setSelection(SizeViewHolder.SELECTED);
//        great.setSelection(SizeViewHolder.UNSELECTED);
//
//        imageBack = getViewById(R.id.i_ab_image_back);
    }

    private void assignListener() {

        mSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSizeText.setSelected(true);
                lSizeText.setSelected(false);
                xlSizeText.setSelected(false);
                refreshViewPager(Bouquet.SIZE_LITTLE);
                initBouquetBySize(Bouquet.SIZE_LITTLE);

            }
        });

        lSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSizeText.setSelected(false);
                lSizeText.setSelected(true);
                xlSizeText.setSelected(false);
                refreshViewPager(Bouquet.SIZE_MEDIUM);
                initBouquetBySize(Bouquet.SIZE_MEDIUM);

            }
        });

        xlSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSizeText.setSelected(false);
                lSizeText.setSelected(false);
                xlSizeText.setSelected(true);
                refreshViewPager(Bouquet.SIZE_GREAT);
                initBouquetBySize(Bouquet.SIZE_GREAT);

            }
        });


        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildOrder();
                goToMapActivity();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        little.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectSize(Bouquet.SIZE_LITTLE);
//            }
//        });
//
//        medium.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectSize(Bouquet.SIZE_MEDIUM);
//            }
//        });
//
//        great.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectSize(Bouquet.SIZE_GREAT);
//            }
//        });
//
//        imageBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                onBackPressed();
//                goToBackActivity();
//            }
//        });

    }


    private void goToBackActivity() {
        startActivity(new Intent(this, BucketsActivity.class));
        Helper.adjustTransition(BucketDetalisActivity.this);
        finish();
    }

    private void initView() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        imageSize = displaymetrics.widthPixels;

        setSupportActionBar(toolbar);
        setTitle("Букет");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));


        Helper.makeTextViewResizable(bouquetDescText, 10, "Читать дальше", true, 10);

        urls = new String[3];
        urls[0] = bouquet.getImageUrl();
        urls[1] = bouquet.getImageUrl();
        urls[2] = bouquet.getImageUrl();



        addBottomDots(CENTER_ITEM_NUMBER);


        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        bouquetViewPagerAdapter = new BouquetViewPagerAdapter(this, urls, displaymetrics.widthPixels);
        bouquetViewPager.setAdapter(bouquetViewPagerAdapter);
        refreshViewPager(DataController.getInstance().getSize());


        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };

        bouquetViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        bouquetViewPager.setCurrentItem(CENTER_ITEM_NUMBER);
        bouquetViewPager.setLayoutParams(new RelativeLayout.LayoutParams(imageSize, imageSize));
//        int size = this.getWindowWidth();
//        Helper.loadImage(this, bouquet.getImageUrl()).resize(size, (int)(size * 1.2))
//                .centerCrop().into(imageBouquet);
//        textPrice.setText(Helper.intToPriceString(bouquet.getMiddleSizePrice(), this));
//        textBouquetName.setText(bouquet.getBouquetNameBySize(currentSize));
    }

    private void refreshViewPager(int size){
        switch (size){
            case Bouquet.SIZE_LITTLE:
                urls[0] = bouquet.getImageSmallUrls().get(0);
                urls[1] = bouquet.getImageSmallUrls().get(1);
                urls[2] = bouquet.getImageSmallUrls().get(2);
                break;
            case Bouquet.SIZE_MEDIUM:
                urls[0] = bouquet.getImageMediumUrls().get(0);
                urls[1] = bouquet.getImageMediumUrls().get(1);
                urls[2] = bouquet.getImageMediumUrls().get(2);
                break;
            case Bouquet.SIZE_GREAT:
                urls[0] = bouquet.getImageLargeUrls().get(0);
                urls[1] = bouquet.getImageLargeUrls().get(1);
                urls[2] = bouquet.getImageLargeUrls().get(2);
                break;
        }
        bouquetViewPagerAdapter.notifyDataSetChanged();
        bouquetViewPager.setCurrentItem(CENTER_ITEM_NUMBER);
    }

    private void selectSize(int sizeId) {
//        if (sizeId != currentSize) {
//            textBouquetName.setText(bouquet.getBouquetNameBySize(sizeId));
//            getSizeHolder(currentSize).setSelection(SizeViewHolder.UNSELECTED);
//            getSizeHolder(sizeId).setSelection(SizeViewHolder.SELECTED);
//            currentSize = sizeId;
//            switch (sizeId) {
//                case Bouquet.SIZE_LITTLE:
//                    textPrice.setText(Helper.intToPriceString(bouquet.getSmallSizePrice(), this));
//                    break;
//                case Bouquet.SIZE_MEDIUM:
//                    textPrice.setText(Helper.intToPriceString(bouquet.getMiddleSizePrice(), this));
//                    break;
//                case Bouquet.SIZE_GREAT:
//                    textPrice.setText(Helper.intToPriceString(bouquet.getLargeSizePrice(), this));
//                    break;
//            }
//
//        }
    }



//    private SizeViewHolder getSizeHolder(int sizeId) {
//        switch (sizeId) {
//            case Bouquet.SIZE_LITTLE:
//                return little;
//            case Bouquet.SIZE_MEDIUM:
//                return medium;
//            case Bouquet.SIZE_GREAT:
//                return great;
//            default:
//                return little;
//        }
//    }

    private class SizeViewHolder {

        public static final int SELECTED = 0;
        public static final int UNSELECTED = 1;

        private TextView text;
        private ImageView imageBig;
        private ImageView imageMin;
        private RelativeLayout relativeLayout;

        private int textColorSelected = R.color.text_color_bouquet_size_selected;
        private int textColorUnselected = R.color.text_color_bouquet_size_unselected;

        private int textSizeSelected = R.dimen.text_size_bouquet_type_selected;
        private int textSizeUnselected = R.dimen.text_size_bouquet_type_unselected;

        private int imageBigSelected = R.drawable.circle_red;
        private int imageBigUnselected = R.drawable.circle_white;

        private int imageMinSelected = R.drawable.round_bouquet_type_selected;
        private int imageMinUnselected = R.drawable.round_bouquet_type_unselected;

        private int imageMarginTopTextSelected = R.dimen.margin_top_bouquet_type_text_selected;
        private int imageMarginTopTextUnselected = R.dimen.margin_top_bouquet_type_text_unselected;

        private int relativeWeightSelected = R.integer.weight_bouquet_type_relative_selected;
        private int relativeWeightUnselected = R.integer.weight_bouquet_type_relative_unselected;

        private int relativeMarginLeftRight = R.dimen.margin_left_right_bouquet_size_relative;

        public SizeViewHolder(TextView text, ImageView imageBig, ImageView imageMin, RelativeLayout relativeLayout) {
            this.text = text;
            this.imageBig = imageBig;
            this.imageMin = imageMin;
            this.relativeLayout = relativeLayout;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            relativeLayout.setOnClickListener(onClickListener);
        }

//        public void setSelection(int selection) {
//            RelativeLayout.LayoutParams layoutParamsText;
//            LinearLayout.LayoutParams layoutParamsRelative;
//            switch (selection) {
//                case SELECTED:
//                    TransitionManager.beginDelayedTransition(getCoordinatorLayout());
//                    layoutParamsText
//                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParamsText.setMargins(0, getResources().getDimensionPixelOffset(imageMarginTopTextSelected), 0, 0);
//                    layoutParamsText.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                    layoutParamsRelative
//                            = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
//                            getResources().getInteger(relativeWeightSelected));
//
//                    layoutParamsRelative.setMargins(getResources().getDimensionPixelSize(relativeMarginLeftRight), 0,
//                            getResources().getDimensionPixelSize(relativeMarginLeftRight), 0);
//
//                    relativeLayout.setLayoutParams(layoutParamsRelative);
//                    relativeLayout.setGravity(Gravity.BOTTOM);
//
//                    text.setLayoutParams(layoutParamsText);
//                    text.setTextColor(getResources().getColor(textColorSelected));
//                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(textSizeSelected));
//
//                    imageBig.setImageResource(imageBigSelected);
//                    imageMin.setImageResource(imageMinSelected);
//                    break;
//                case UNSELECTED:
//                    TransitionManager.beginDelayedTransition(getCoordinatorLayout());
//                    layoutParamsText
//                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParamsText.setMargins(0, getResources().getDimensionPixelOffset(imageMarginTopTextUnselected), 0, 0);
//                    layoutParamsText.addRule(RelativeLayout.CENTER_HORIZONTAL);
//
//                    layoutParamsRelative
//                            = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
//                            getResources().getInteger(relativeWeightUnselected));
//
//                    layoutParamsRelative.setMargins(getResources().getDimensionPixelSize(relativeMarginLeftRight), 0,
//                            getResources().getDimensionPixelSize(relativeMarginLeftRight), 0);
//
//                    relativeLayout.setLayoutParams(layoutParamsRelative);
//                    relativeLayout.setGravity(Gravity.BOTTOM);
//
//                    text.setLayoutParams(layoutParamsText);
//                    text.setTextColor(getResources().getColor(textColorUnselected));
//                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(textSizeUnselected));
//
//                    imageBig.setImageResource(imageBigUnselected);
//                    imageMin.setImageResource(imageMinUnselected);
//                    break;
//            }
//        }
    }

    private void buildOrder() {
        Order order = new Order();

        order.setSizeIndex(currentSize);
        order.setSize(Bouquet.getSizeDesc(currentSize));
        order.setBouquetItemId(bouquet.getId());
        order.setBouquetItem(bouquet);
        order.setPrice(bouquet.getBouquetPriceBySize(currentSize));

        DataController.getInstance().setOrder(order);
    }

    private void goToMapActivity() {
        Helper.gotoActivity(this, DeliveryInfoFillingActivity.class);
    }

    private void initBouquetBySize(int size){

        /*Helper.loadImage(this, bouquet.getImageUrl())
                .placeholder(R.drawable.image_placeholder).fit().centerCrop()
                .into(bouquetImage);*/



        int a = toolbar.getHeight();
        a = sizeLayout.getHeight();
        a = imageSize;
        int ratingSmallMargin = imageSize - bouquetRatingSmallText.getHeight() / 2;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bouquetRatingSmallText.getLayoutParams();
        params.setMargins(0, ratingSmallMargin, 20, 0);
        bouquetRatingSmallText.setLayoutParams(params);

        bouquetTitleText.setText(bouquet.getBouquetNameBySize(size));





        int index = bouquet.getDescription().indexOf("|");
        if (index != -1) {
            String elements = bouquet.getDescription().substring(0, index);
            String desc = bouquet.getDescription().substring(index + 1);
            bouquetDescText.setText(desc);
            bouquetCompText.setText(elements);

        } else {
            bouquetDescText.setText(bouquet.getDescription());
            bouquetCompText.setText(bouquet.getDescription());
        }


        bouquetRatingText.setText("4.5");
        buyButton.setText(BUY_STRING_BEFORE + bouquet.getBouquetPriceBySize(size) + BUY_STRING_AFTER);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int size = DataController.getInstance().getSize();
        switch (size){
            case Bouquet.SIZE_LITTLE:
                mSizeText.setSelected(true);
                break;
            case Bouquet.SIZE_MEDIUM:
                lSizeText.setSelected(true);
                break;
            case Bouquet.SIZE_GREAT:
                xlSizeText.setSelected(true);
                break;
        }
        initBouquetBySize(size);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[urls.length];


        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226; "));
            dots[i].setTextSize(20);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.color_dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.color_dot_active));
        }
    }

    private void goToSocialActivity() {
        startActivity(new Intent(this, SocialActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                goToSocialActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
