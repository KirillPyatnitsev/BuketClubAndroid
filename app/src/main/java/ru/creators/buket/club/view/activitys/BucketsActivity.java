package ru.creators.buket.club.view.activitys;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import ru.creators.buket.club.R;
import ru.creators.buket.club.tools.Helper;

public class BucketsActivity extends BaseActivity {

    private ImageView imageOpenFilter;
    private ImageView imageCloseFilter;
    private ImageView imageActionBarBackground;

    private TextView textActionBarTitle;
    private View viewActonBarFilter;

    private TextView textCurrentCostMin;
    private TextView textCurrentCostMax;

    private TextView textCostMix;
    private TextView textCostMax;

    private GridView gridView;

    private RangeSeekBar rangeSeekBarCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buckets);

        assignView();
        assignListener();
        initView();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_b_coordinator_root;
    }

    private void assignView(){
        imageOpenFilter = getViewById(R.id.i_ab_image_settings_open);
        imageCloseFilter = getViewById(R.id.i_ab_image_settings_close);
        imageActionBarBackground = getViewById(R.id.a_b_image_action_bar_background);

        rangeSeekBarCost = getViewById(R.id.a_b_seek_bar);

        textActionBarTitle = getViewById(R.id.a_b_text_action_bar_title);
        viewActonBarFilter = getViewById(R.id.a_b_view_filter);

        textCurrentCostMin = getViewById(R.id.a_b_text_current_cost_min);
        textCurrentCostMax = getViewById(R.id.a_b_text_current_cost_max);

        textCostMix = getViewById(R.id.a_b_text_cost_min);
        textCostMax = getViewById(R.id.a_b_text_cost_max);

        gridView = getViewById(R.id.a_b_grid_view_buckets);
    }

    private void assignListener(){
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
                textCurrentCostMin.setText(Helper.getStringWithCostPrefix((int) minValue, getContext()));
                textCurrentCostMax.setText(Helper.getStringWithCostPrefix((int) maxValue, getContext()));
            }
        });
    }

    private void initView(){
        imageOpenFilter.setVisibility(View.VISIBLE);
        setSpinnerData(650, 7490);
    }

    private void openFilter(){
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

    private void closeFilter(){
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
    }

    private void setSpinnerData(int min, int max){
        rangeSeekBarCost.setRangeValues(min, max);
        rangeSeekBarCost.setSelectedMinValue(min);
        rangeSeekBarCost.setSelectedMaxValue(max);

        textCostMix.setText(Helper.getStringWithCostPrefix(min, this));
        textCostMax.setText(Helper.getStringWithCostPrefix(max, this));

        textCurrentCostMin.setText(Helper.getStringWithCostPrefix(min, this));
        textCurrentCostMax.setText(Helper.getStringWithCostPrefix(max, this));
    }

    private Context getContext(){
        return this;
    }

    @Override
    protected int getContentContainerId() {
        return R.id.a_b_relative_content_container;
    }

    @Override
    protected int getImageBlurId() {
        return R.id.a_b_blur_image;
    }
}
