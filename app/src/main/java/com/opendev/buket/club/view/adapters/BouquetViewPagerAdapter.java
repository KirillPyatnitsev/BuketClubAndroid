package com.opendev.buket.club.view.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.opendev.buket.club.R;
import com.opendev.buket.club.tools.Helper;

/**
 * Created by Danis on 29.08.2016.
 */
public class BouquetViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private String[] urls;
    private int imageSize;

    public BouquetViewPagerAdapter(Context context, String[] urls, int imageSize) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.urls = urls;
        this.imageSize = imageSize;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        View view = layoutInflater.inflate(R.layout.viewpager_bouquet_image, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.viewpager_bouquet_img);

        switch (position) {
            case 0:
                Helper.loadImage(context, urls[0]).resize(imageSize, imageSize)
                        .placeholder(R.drawable.placeholder)
                        .centerCrop().into(img);
                break;
            case 1:
                Helper.loadImage(context, urls[1]).resize(imageSize, imageSize)
                        .placeholder(R.drawable.placeholder)
                        .centerCrop().into(img);
                break;
            case 2:
                Helper.loadImage(context, urls[2]).resize(imageSize, imageSize)
                        .placeholder(R.drawable.placeholder)
                        .centerCrop().into(img);
                break;
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
