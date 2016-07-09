package ru.creators.buket.club.view.activitys;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

import ru.creators.buket.club.R;

public class ArtistsBouquetsActivity extends BaseActivity {

    private ImageView imageOpenFilter;
    private ImageView imageCloseFilter;
    private ImageView imageBack;
    private ImageView imageActionBarBackground;

    private TextView textActionBarTitle;
    private View viewActonBarFilter;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_artists_bouquets);

        assignView();
        assignListener();
        initView();
    }

    private void assignView() {
        imageOpenFilter = getViewById(R.id.i_ab_image_settings_open);
        imageCloseFilter = getViewById(R.id.i_ab_image_settings_close);
        imageBack = getViewById(R.id.i_ab_image_back);
        imageActionBarBackground = getViewById(R.id.a_ab_image_action_bar_background);

        textActionBarTitle = getViewById(R.id.a_ab_text_action_bar_title);
        viewActonBarFilter = getViewById(R.id.a_ab_view_filter);
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

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        imageOpenFilter.setVisibility(View.VISIBLE);
        imageBack.setVisibility(View.VISIBLE);
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
                getResources().getDimensionPixelOffset(R.dimen.margin_top_action_bar_a_ab_close),
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                0
        );

        imageActionBarBackground.setLayoutParams(backgroundImageLayoutParams);

        textActionBarTitle.setVisibility(View.VISIBLE);
        viewActonBarFilter.setVisibility(View.GONE);

        imageOpenFilter.setVisibility(View.VISIBLE);
        imageCloseFilter.setVisibility(View.GONE);
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_ab_coordinator_root;
    }

    @Override
    protected int getImageBlurId() {
        return R.id.a_ab_blur_image;
    }

    @Override
    protected int getContentContainerId() {
        return R.id.a_ab_relative_content_container;
    }
}
