package com.opendev.buket.club.view.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.lists.ListOrder;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.view.activity.MapActivity;

import java.util.ArrayList;
import java.util.List;


public class ListOrderAdapter extends BaseAdapter {

    private final Context context;
    private final List<Order> orders = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public ListOrderAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public final int getCount() {
        return orders.size();
    }

    @Override
    public final Order getItem(int position) {
        return orders.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View itemView;
        if (convertView == null) {
            itemView = layoutInflater.inflate(R.layout.list_orders_item, parent, false);

            holder = new ViewHolder();
            itemView.setTag(holder);
            holder.title = getViewById(R.id.list_orders_title, itemView);
            holder.image = getViewById(R.id.list_orders_image, itemView);
            holder.status = getViewById(R.id.list_orders_shop, itemView);
            holder.options = getViewById(R.id.list_orders_options, itemView);
            holder.optionsLayout = getViewById(R.id.list_orders_options_layout, itemView);
            holder.price = getViewById(R.id.list_orders_price, itemView);
            holder.number = getViewById(R.id.list_orders_number2, itemView);
            holder.backgroundLayout = getViewById(R.id.list_orders_layout, itemView);
        } else {
            itemView = convertView;
            holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                Crashlytics.log("ViewHolder is null in reused view! " + convertView);
            }
        }

        Order order = getItem(position);
        holder.title.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));
        holder.price.setText(Helper.intToPriceString(order.getPrice(), context));
        holder.status.setText(context.getString(order.getStatusDescRes()));
        holder.number.setText("№" + String.valueOf(order.getId()));

        String status = order.getStatus();


        if (status.equals("finding_shop")) {
            holder.status.setText("Поиск исполнителя...");
            holder.backgroundLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.order_background_gray));
        } else {
            holder.status.setText(order.getShop().getName());
            holder.backgroundLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        int size = parent.getWidth() / 2;
        holder.image.setVisibility(View.VISIBLE);
        Helper.loadImage(context, order.getBouquetItem().getImagesBySize(order.getSizeIndex()).get(0)).resize(size, size)
                .placeholder(R.drawable.placeholder).centerCrop().into(holder.image);

        holder.optionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomDialog(position);
            }
        });


        return itemView;
    }

    private <T extends View> T getViewById(int id, View root) {
        return (T) root.findViewById(id);
    }

    public final Order get(int position) {
        Order order = this.orders.get(position);
        return order;
    }

    public void clear() {
        this.orders.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(ListOrder listOrder) {
        for(Order order: listOrder) {
            boolean valid = isValid(order);
            if(valid) {
                this.orders.add(order);
            }
        }
        this.notifyDataSetChanged();
    }

    private static boolean isValid(Order order) {
        if(order == null) {
            return false;
        }
        // ...
        return true;
    }

    private static class ViewHolder {
        LinearLayout backgroundLayout;
        TextView number;
        TextView title;
        ImageView options;
        RelativeLayout optionsLayout;
        TextView price;
        TextView status;
        ImageView image;
    }

    private void launchCustomDialog(final int position){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.orders_dialog_delivered_menu);

        RelativeLayout buyLayout = (RelativeLayout) dialog.findViewById(R.id.shop_on_the_map_layout);
        buyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataController.getInstance().setOrder(orders.get(position));
                goToMapActivity();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void goToMapActivity() {
        Helper.gotoActivity((Activity) context, MapActivity.class);
    }


}
