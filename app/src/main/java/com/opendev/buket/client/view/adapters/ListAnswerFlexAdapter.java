package com.opendev.buket.client.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.opendev.buket.client.R;
import com.opendev.buket.client.model.AnswerFlex;
import com.opendev.buket.client.model.Shop;
import com.opendev.buket.client.model.lists.ListAnswerFlex;
import com.opendev.buket.client.tools.Helper;

/**
 * Created by mifkamaz on 14/12/15.
 */
public class ListAnswerFlexAdapter extends BaseAdapter {

    private Context context;
    private ListAnswerFlex listAnswerFlex;
    private LayoutInflater layoutInflater;

    public ListAnswerFlexAdapter(Context context, ListAnswerFlex listOrder) {
        this.context = context;
        this.listAnswerFlex = listOrder;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listAnswerFlex.size();
    }

    @Override
    public AnswerFlex getItem(int position) {
        return listAnswerFlex.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.inflate_artist_bouquet, parent, false);

            holder = new ViewHolder();

            holder.textArtistName = getViewById(R.id.i_ab_text_artist_name, convertView);
            holder.textBouquetCost = getViewById(R.id.i_ab_text_cost, convertView);
            holder.textDistance = getViewById(R.id.i_ab_text_distance, convertView);
            holder.ratingBar = getViewById(R.id.i_ab_rating_bar, convertView);
            holder.imageArtistLogo = getViewById(R.id.i_ab_image_artist, convertView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AnswerFlex answerFlex = getItem(position);

        if (answerFlex != null) {
            Shop shop = answerFlex.getShop();
            if (shop != null && shop.getImageUrl() != null) {
                int size = parent.getWidth();
                Helper.loadImage(context, shop.getImageUrl()).resize(size, size)
                        .centerCrop().into(holder.imageArtistLogo);
            }

            holder.textArtistName.setText(answerFlex.getShop().getName());
            holder.textBouquetCost.setText(Helper.intToPriceString(answerFlex.getPrice(), context));
            holder.ratingBar.setRating(answerFlex.getShop().getCachedRating());
            holder.textDistance.setText(distToString(answerFlex.getDistance()));
        }

        return convertView;
    }

    private <T extends View> T getViewById(int id, View root) {
        return (T) root.findViewById(id);
    }

    private class ViewHolder {
        TextView textArtistName;
        TextView textBouquetCost;
        TextView textDistance;
        RatingBar ratingBar;
        ImageView imageArtistLogo;
    }

    private String distToString(float distMeters) {
        if (distMeters >= 1000) {
            return Integer.toString((int) distMeters / 1000) + " " + context.getString(R.string.kilometer);
        } else {
            return Integer.toString((int) distMeters) + " " + context.getString(R.string.meter);
        }
    }
}
