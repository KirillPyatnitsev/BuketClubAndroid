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

import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.model.Bouquet;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.lists.ListBouquet;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.view.activity.BucketDetalisActivity;
import com.opendev.buket.club.view.activity.DeliveryInfoFillingActivity;
import com.opendev.buket.club.view.activity.SocialActivity;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class GridAdapterBouquet extends BaseAdapter {

    private ListBouquet listBouquet;
    private Context context;
    private int size;

    private int imageSize;

    public GridAdapterBouquet(ListBouquet listBouquet, Context context, int size, int screenWidth) {
        this.listBouquet = listBouquet;
        this.context = context;
        this.size = size;
        this.imageSize = screenWidth / 2;
    }

    @Override
    public int getCount() {
        return listBouquet.size();
    }

    @Override
    public Bouquet getItem(int position) {
        return listBouquet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView image;
        TextView title;
        ImageView options;
        RelativeLayout optionsLayout;
        TextView price;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(convertView == null){

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item_bouquet, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.grid_bouquet_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.grid_bouquet_title);
            viewHolder.options = (ImageView) convertView.findViewById(R.id.grid_bouquet_options);
            viewHolder.optionsLayout = (RelativeLayout) convertView.findViewById(R.id.grid_bouquet_options_layout);
            viewHolder.price = (TextView) convertView.findViewById(R.id.grid_bouquet_price);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams params = viewHolder.image.getLayoutParams();
        params.height = imageSize;
        viewHolder.image.setLayoutParams(params);



        Helper.loadImage(context, getItem(position).getImagesBySize(size).get(0))
                .resize(imageSize, imageSize)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(viewHolder.image);
        //viewHolder.sliderLayout.setLayoutParams(new ViewGroup.LayoutParams(imageSize, imageSize));


        viewHolder.title.setText(getItem(position).getBouquetNameBySize(size));
        String text = String.valueOf(getItem(position).getBouquetPriceBySize(size)) + " руб.";
        viewHolder.price.setText(text);

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

 /*   private ListBouquet listBouquet;
    private Context context;
    private int size;

    private int imageSize;

    public GridAdapterBouquet(ListBouquet listBouquet, Context context, int size, int screenWidth) {
        this.listBouquet = listBouquet;
        this.context = context;
        this.size = size;
        this.imageSize = screenWidth / 2;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_bouquet, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemHolder itemHolder = (ItemHolder) holder;
        Bouquet wine = listBouquet.get(position);

        Helper.loadImage(context, listBouquet.get(position).getImageUrl()).resize(imageSize, imageSize)
             //   .placeholder(R.drawable.placeholder)
                .centerCrop().into(itemHolder.image);
        //viewHolder.sliderLayout.setLayoutParams(new ViewGroup.LayoutParams(imageSize, imageSize));


        itemHolder.title.setText(listBouquet.get(position).getBouquetNameBySize(size));
        String text = String.valueOf(listBouquet.get(position).getBouquetPriceBySize(size)) + " руб.";
        itemHolder.price.setText(text);

    }


    @Override
    public int getItemCount() {
        return listBouquet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;

    }



    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        ImageView options;
        TextView price;

        public ItemHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.grid_bouquet_image);
            title = (TextView) itemView.findViewById(R.id.grid_bouquet_title);
            options = (ImageView) itemView.findViewById(R.id.grid_bouquet_options);
            price = (TextView) itemView.findViewById(R.id.grid_bouquet_price);
        }

    }*/


}


