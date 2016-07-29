package com.opendev.buket.client.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import com.opendev.buket.client.R;
import com.opendev.buket.client.model.Order;
import com.opendev.buket.client.model.Shop;
import com.opendev.buket.client.model.lists.ListOrder;
import com.opendev.buket.client.tools.Helper;

/**
 * Created by mifkamaz on 14/12/15.
 */
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View itemView;
        if (convertView == null) {
            itemView = layoutInflater.inflate(R.layout.inflate_order_item, parent, false);

            holder = new ViewHolder();
            itemView.setTag(holder);
            holder.textBouquetName = getViewById(R.id.i_oi_text_bouquet_name, itemView);
            holder.textBouquetCost = getViewById(R.id.i_oi_text_bouquet_cost, itemView);
            holder.textOrderStatus = getViewById(R.id.i_oi_text_order_status, itemView);
            holder.imageArtistLogo = getViewById(R.id.i_oi_image_artist, itemView);
        } else {
            itemView = convertView;
            holder = (ViewHolder) convertView.getTag();
            if(holder == null) {
                Crashlytics.log("ViewHolder is null in reused view! " + convertView);
            }
        }

        Order order = getItem(position);
        holder.textBouquetName.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));
        holder.textBouquetCost.setText(Helper.intToPriceString(order.getPrice(), context));
        holder.textOrderStatus.setText(context.getString(order.getStatusDescRes()));
        Shop shop = order.getShop();
        if (shop != null && shop.getImageUrl() != null) {
            int size = parent.getWidth() / 3;
            holder.imageArtistLogo.setVisibility(View.VISIBLE);
            Helper.loadImage(context, shop.getImageUrl()).resize(size, size).centerCrop()
                    .into(holder.imageArtistLogo);
        } else {
            holder.imageArtistLogo.setVisibility(View.GONE);
        }

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
        TextView textBouquetName;
        TextView textBouquetCost;
        TextView textOrderStatus;
        ImageView imageArtistLogo;
    }
}
