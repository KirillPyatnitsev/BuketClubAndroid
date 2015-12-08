package ru.creators.buket.club.view.activitys;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Bouquet;

public class BucketDetalisActivity extends BaseActivity {

    private int currentSize = Bouquet.SIZE_MEDIUM;

    private TextView textBouquetSizeLittle;
    private TextView textBouquetSizeMedium;
    private TextView textBouquetSizeGreat;

    private ImageView imageBouquetSizeLittleBig;
    private ImageView imageBouquetSizeMediumBig;
    private ImageView imageBouquetSizeGreatBig;

    private ImageView imageBouquetSizeLittleMin;
    private ImageView imageBouquetSizeMediumMin;
    private ImageView imageBouquetSizeGreatMin;

    private RelativeLayout relativeBouquetSizeLittle;
    private RelativeLayout relativeBouquetSizeMedium;
    private RelativeLayout relativeBouquetSizeGreat;

    private SizeViewHolder little;
    private SizeViewHolder medium;
    private SizeViewHolder great;

    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_detalis);
        assignView();
        assignListener();

        imageBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_bd_coordinator_root;
    }

    private void assignView() {
        textBouquetSizeLittle = getViewById(R.id.a_bd_text_bouquet_size_little);
        imageBouquetSizeLittleBig = getViewById(R.id.a_bd_image_big_bouquet_size_little);
        imageBouquetSizeLittleMin = getViewById(R.id.a_bd_image_min_bouquet_size_little);
        relativeBouquetSizeLittle = getViewById(R.id.a_bd_relative_bouquet_little);

        textBouquetSizeMedium = getViewById(R.id.a_bd_text_bouquet_size_medium);
        imageBouquetSizeMediumBig = getViewById(R.id.a_bd_image_big_bouquet_size_medium);
        imageBouquetSizeMediumMin = getViewById(R.id.a_bd_image_min_bouquet_size_medium);
        relativeBouquetSizeMedium = getViewById(R.id.a_bd_relative_bouquet_medium);

        textBouquetSizeGreat = getViewById(R.id.a_bd_text_bouquet_size_great);
        imageBouquetSizeGreatBig = getViewById(R.id.a_bd_image_big_bouquet_size_great);
        imageBouquetSizeGreatMin = getViewById(R.id.a_bd_image_min_bouquet_size_great);
        relativeBouquetSizeGreat = getViewById(R.id.a_bd_relative_bouquet_great);

        little = new SizeViewHolder(textBouquetSizeLittle, imageBouquetSizeLittleBig, imageBouquetSizeLittleMin, relativeBouquetSizeLittle);
        medium = new SizeViewHolder(textBouquetSizeMedium, imageBouquetSizeMediumBig, imageBouquetSizeMediumMin, relativeBouquetSizeMedium);
        great = new SizeViewHolder(textBouquetSizeGreat, imageBouquetSizeGreatBig, imageBouquetSizeGreatMin, relativeBouquetSizeGreat);

        imageBack = getViewById(R.id.i_ab_image_back);
    }

    private void assignListener() {
        little.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSize(Bouquet.SIZE_LITTLE);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSize(Bouquet.SIZE_MEDIUM);
            }
        });

        great.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSize(Bouquet.SIZE_GREAT);
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void selectSize(int sizeId) {
        if (sizeId != currentSize) {
            getSizeHolder(currentSize).setSelection(SizeViewHolder.UNSELECTED);
            getSizeHolder(sizeId).setSelection(SizeViewHolder.SELECTED);
            currentSize = sizeId;
        }
    }

    private SizeViewHolder getSizeHolder(int sizeId) {
        switch (sizeId) {
            case Bouquet.SIZE_LITTLE:
                return little;
            case Bouquet.SIZE_MEDIUM:
                return medium;
            case Bouquet.SIZE_GREAT:
                return great;
            default:
                return little;
        }
    }

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

        private int imageBigSelected = R.drawable.round_bouqet_type_big_selected;
        private int imageBigUnselected = R.drawable.round_bouqet_type_big_unselected;

        private int imageMinSelected = R.drawable.round_bouquet_type_selected;
        private int imageMinUnselected = R.drawable.round_buqet_type_unselected;

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

        public void setSelection(int selection) {
            RelativeLayout.LayoutParams layoutParamsText;
            LinearLayout.LayoutParams layoutParamsRelative;
            switch (selection) {
                case SELECTED:
                    TransitionManager.beginDelayedTransition(relativeLayout);
                    layoutParamsText
                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParamsText.setMargins(0, getResources().getDimensionPixelOffset(imageMarginTopTextSelected), 0, 0);
                    layoutParamsText.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    layoutParamsRelative
                            = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                            getResources().getInteger(relativeWeightSelected));

                    layoutParamsRelative.setMargins(getResources().getDimensionPixelSize(relativeMarginLeftRight), 0,
                            getResources().getDimensionPixelSize(relativeMarginLeftRight), 0);

                    relativeLayout.setLayoutParams(layoutParamsRelative);

                    text.setLayoutParams(layoutParamsText);
                    text.setTextColor(getResources().getColor(textColorSelected));
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(textSizeSelected));

                    imageBig.setImageResource(imageBigSelected);
                    imageMin.setImageResource(imageMinSelected);
                    break;
                case UNSELECTED:
                    TransitionManager.beginDelayedTransition(relativeLayout);
                    layoutParamsText
                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParamsText.setMargins(0, getResources().getDimensionPixelOffset(imageMarginTopTextUnselected), 0, 0);
                    layoutParamsText.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    layoutParamsRelative
                            = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                            getResources().getInteger(relativeWeightUnselected));

                    layoutParamsRelative.setMargins(getResources().getDimensionPixelSize(relativeMarginLeftRight), 0,
                            getResources().getDimensionPixelSize(relativeMarginLeftRight), 0);

                    relativeLayout.setLayoutParams(layoutParamsRelative);

                    text.setLayoutParams(layoutParamsText);
                    text.setTextColor(getResources().getColor(textColorUnselected));
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(textSizeUnselected));

                    imageBig.setImageResource(imageBigUnselected);
                    imageMin.setImageResource(imageMinUnselected);
                    break;
            }
        }
    }
}
