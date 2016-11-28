package com.opendev.buket.club.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Bouquet;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.DefaultResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PayDoneActivity extends BaseActivity {

    private static final String TAG = ServerConfig.TAG_PREFIX + "PayDoneActivity";

    //private TextView textViewPriceBouquet;
    //private TextView textViewTitleBouquet;
    //private TextView textViewCompositionBouquet;
    private TextView textViewOrderId;
    //private TextView textViewShopAddress;
    //private TextView textViewShopPhone;
    private ImageView facebookIcon;
    private ImageView twitterIcon;
    private ImageView instagramIcon;
    private ImageView googlePlusIcon;

    private ImageView sourceImage;
    //private ImageView imageViewBouquet;

    private Timer timer = null;
    private Bouquet bouquet;

    private Order order;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_pay_done);
        order = DataController.getInstance().getOrder();

        if(order == null) {
            this.finish();
        }
        assignView();
        assignListener();
        initView();
        sendOrder();
    }

    private void assignView() {
        //textViewPriceBouquet = getViewById(R.id.a_pd_price);
        //textViewTitleBouquet = getViewById(R.id.a_pd_title_bouquet);
        //textViewCompositionBouquet = getViewById(R.id.a_pd_composition_bouquet);
        textViewOrderId = getViewById(R.id.a_pd_id_order);
        //textViewShopAddress = getViewById(R.id.a_pd_shop_address);
        //textViewShopPhone = getViewById(R.id.a_pd_shop_phone);
        facebookIcon = getViewById(R.id.facebook_icon);
        twitterIcon = getViewById(R.id.twitter_icon);
        instagramIcon = getViewById(R.id.instagram_icon);
        googlePlusIcon = getViewById(R.id.google_plus_icon);

        sourceImage = getViewById(R.id.share_source_image);
        //imageViewBouquet = getViewById(R.id.a_pd_image_bouquet);
    }

    private void assignListener(){
        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareItem();
            }
        });

        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareItem();
            }
        });

        instagramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareItem();
            }
        });

        googlePlusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareItem();
            }
        });
    }

    private void startClosingTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                goToOrderDetails();
                // If you want to call Activity then call from here for 5 seconds it automatically call and your image disappear....
            }
        }, 5000);
    }

    private void initView(){
        bouquet = DataController.getInstance().getBouquet();
        Helper.loadImage(this, bouquet.getImageUrl()).resize(400, 400).centerCrop()
                .into(sourceImage);

    }

    private void goToOrderDetails() {
        startActivity(new Intent(this, OrdersActivity.class));
    }


    public void onShareItem() {
        // Get access to bitmap image from view
        ImageView ivImage = (ImageView) findViewById(R.id.share_source_image);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Самый лучший букет я купил через приложение buket.club. Советую!");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("*/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    private void sendOrder() {

        WebMethods.getInstance().orderPatchRequest(order.getOrderForServer(), order.getId(), new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(PayDoneActivity.this, "Есть проблемы с размещением заказа!", Toast.LENGTH_LONG).show();
                startClosingTimer();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                startClosingTimer();
            }
        });

        int sizeIndex = order.getSizeIndex();
        //textViewPriceBouquet.setText(String.valueOf(order.getPrice()));
        //textViewTitleBouquet.setText(order.getBouquetItem().getBouquetNameBySize(sizeIndex));
        //textViewCompositionBouquet.setText(order.getBouquetItem().getBouquetDescriptionBySize(sizeIndex));
        textViewOrderId.setText(getString(R.string.number_order, order.getId()));

        //int size = this.getWindowWidth();
        //Helper.loadImage(this, order.getBouquetItem().getImageUrl()).resize(size, size)
        //        .centerCrop().into(imageViewBouquet);
        //Helper.drawSizeOnImage(order.getSizeIndex(), imageViewBouquet);

        //Shop shop = order.getShop();
        //if (shop != null) {
        //    textViewShopAddress.setText(shop.getName());
        //    textViewShopPhone.setText(shop.getPhone());
        //}

    }

    @Override
    public void onBackPressed() {
        if(timer != null) {
            timer.cancel();
        }
        goToBuketActivity();
    }

    private void goToBuketActivity() {
        startActivity(new Intent(this, BucketsActivity.class));
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pd_coordinator_root;
    }
}
