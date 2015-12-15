package ru.creators.buket.club.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.lists.ListOrder;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.web.WebMethods;

/**
 * Created by mifkamaz on 14/12/15.
 */
public class ListOrderAdapter extends BaseAdapter {

    private Context context;
    private ListOrder listOrder;
    private LayoutInflater layoutInflater;

    public ListOrderAdapter(Context context, ListOrder listOrder) {
        this.context = context;
        this.listOrder = listOrder;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listOrder.size();
    }

    @Override
    public Order getItem(int position) {
        return listOrder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.inflate_order_item, parent, false);

            holder = new ViewHolder();

            holder.textBouquetName = getViewById(R.id.i_oi_text_bouquet_name, convertView);
            holder.textBouquetCost = getViewById(R.id.i_oi_text_bouquet_cost, convertView);
            holder.textOrderStatus = getViewById(R.id.i_oi_text_order_status, convertView);
            holder.imageArtistLogo = getViewById(R.id.i_oi_image_open, convertView);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = getItem(position);

        if (order!=null){
            holder.textBouquetName.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));
            holder.textBouquetCost.setText(Helper.intToPriceString(order.getPrice()));
            holder.textOrderStatus.setText(context.getString(order.getStatusDescRes()));
            if (order.getStatusIndex() != Order.STATUS_FILLING_SHOP_INDEX )
                WebMethods.getInstance().loadImage(context, Helper.addServerPrefix(order.getShop().getImageUrl()), holder.imageArtistLogo);
        }

        return convertView;
    }

    private <T extends View> T getViewById(int id, View root){
        return (T) root.findViewById(id);
    }

    private class ViewHolder{
        TextView textBouquetName;
        TextView textBouquetCost;
        TextView textOrderStatus;
        ImageView imageArtistLogo;
    }
}
