package com.opendev.buket.club.view.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.model.Bouquet;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.lists.ListBouquet;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.view.activity.BucketDetalisActivity;
import com.opendev.buket.club.view.activity.DeliveryInfoFillingActivity;
import com.opendev.buket.club.view.activity.SocialActivity;


public class ListAdapterBouquet extends BaseAdapter implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private ListBouquet listBouquet;
    private Context context;
    private int size;

    private int imageSize;

    public ListAdapterBouquet(ListBouquet listBouquet, Context context, int size, int screenWidth) {
        this.listBouquet = listBouquet;
        this.context = context;
        this.size = size;
        this.imageSize = screenWidth;
    }

    @Override
    public int getCount() {
        return listBouquet.size();
    }

    @Override
    public Bouquet getItem(int i) {
        return listBouquet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    static class ViewHolder {
        ImageView image;
        TextView title;
        ImageView options;
        RelativeLayout optionsLayout;
        TextView description;
        TextView price;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(convertView == null){

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_bouquet, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.list_bouquet_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_bouquet_title);
            viewHolder.options = (ImageView) convertView.findViewById(R.id.list_bouquet_options);
            viewHolder.optionsLayout = (RelativeLayout) convertView.findViewById(R.id.list_bouquet_options_layout);
            viewHolder.description = (TextView) convertView.findViewById(R.id.list_bouquet_desc);
            viewHolder.price = (TextView) convertView.findViewById(R.id.list_bouquet_price);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Helper.loadImage(context, getItem(position).getImagesBySize(size).get(0)).resize(imageSize, imageSize)
                .placeholder(R.drawable.placeholder)
                .centerCrop().into(viewHolder.image);
        //viewHolder.sliderLayout.setLayoutParams(new ViewGroup.LayoutParams(imageSize, imageSize));

        int index = getItem(position).getDescription().indexOf("|");
        if (index != -1) {
            String elements = getItem(position).getDescription().substring(0, index);
            viewHolder.description.setText(elements);
        } else {
            viewHolder.description.setText(getItem(position).getDescription());
        }
        viewHolder.title.setText(getItem(position).getBouquetNameBySize(size));
        viewHolder.price.setText(String.valueOf(getItem(position).getBouquetPriceBySize(size)) + " руб.");

        viewHolder.optionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomDialog(position);
            }
        });

        return convertView;
    }


    private void launchCustomDialog(final int position){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_bouquet_menu);

        RelativeLayout buyLayout = (RelativeLayout) dialog.findViewById(R.id.buy_layout);
        buyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataController.getInstance().setBouquet(listBouquet.get(position));
                buildOrder(position);
                goToDeliveryActivity();
                dialog.dismiss();
            }
        });

        RelativeLayout moreLayout = (RelativeLayout) dialog.findViewById(R.id.more_layout);
        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataController.getInstance().setBouquet(listBouquet.get(position));
                DataController.getInstance().setSize(size);
                goToBouquetDetailsActivity();
                dialog.dismiss();
            }
        });

        RelativeLayout shareLayout = (RelativeLayout) dialog.findViewById(R.id.share_layout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataController.getInstance().setBouquet(listBouquet.get(position));
                DataController.getInstance().setSize(size);
                goToSocialActivity();
                dialog.dismiss();
            }
        });




        dialog.show();
    }

    private void goToDeliveryActivity() {
        Helper.gotoActivity((Activity) context, DeliveryInfoFillingActivity.class);
    }

    private void goToBouquetDetailsActivity() {
        Helper.gotoActivity((Activity) context, BucketDetalisActivity.class);
    }

    private void goToSocialActivity() {
        Helper.gotoActivity((Activity) context, SocialActivity.class);
    }

    private void buildOrder(int position) {
        Order order = new Order();

        Bouquet bouquet = listBouquet.get(position);

        order.setSizeIndex(size);
        order.setSize(Bouquet.getSizeDesc(size));
        order.setBouquetItemId(bouquet.getId());
        order.setBouquetItem(bouquet);
        order.setPrice(bouquet.getBouquetPriceBySize(size));

        DataController.getInstance().setOrder(order);
    }




}
