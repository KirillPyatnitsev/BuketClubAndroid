package ru.creators.buket.club.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Bouquet;
import ru.creators.buket.club.model.lists.ListBouquet;
import ru.creators.buket.club.tools.Helper;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class GridAdapterBouquet extends BaseAdapter {

    private ListBouquet listBouquet;
    private Context context;

    private int imageSize;

    public GridAdapterBouquet(ListBouquet listBouquet, Context context, int screenWidth) {
        this.listBouquet = listBouquet;
        this.context = context;
        imageSize = screenWidth / 2;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        int size = parent.getWidth() / 2;
        Helper.loadImage(context, getItem(position).getImageUrl()).resize(size, size)
                .centerCrop().into(imageView);
        return imageView;
    }

}
