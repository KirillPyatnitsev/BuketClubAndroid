package com.opendev.buket.club.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.model.Bouquet;
import com.opendev.buket.club.tools.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class SocialActivity extends BaseActivity {
    private Button shareThenButton;

    private ImageView shareIcon;

    private ImageView sourceImage;
    private Bouquet bouquet;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_social);

        assignView();
        assignListener();
        initView();
    }

    private void assignView(){
        shareThenButton = getViewById(R.id.share_then_button);
        shareIcon = getViewById(R.id.social_share_icon);
        sourceImage = getViewById(R.id.share_source_image);
        relativeLayout = getViewById(R.id.social_bottom_layout);

    }

    private void assignListener(){
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareItem();
            }
        });



        shareThenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void initView(){

        bouquet = DataController.getInstance().getBouquet();
        Helper.loadImage(this, bouquet.getImageUrl()).resize(400, 400).centerCrop()
                .into(sourceImage);



    }

   // private void shareTwitter(){
    //    Intent intent = new Intent();
    //    intent.setAction(Intent.ACTION_SEND);

   //     intent.putExtra(Intent.EXTRA_TEXT, "Hello");
    //    intent.putExtra(Intent.EXTRA_STREAM, Helper.addServerPrefix(bouquet.getImageUrl()));
     //   intent.setType("*/*");
        //startActivity(Intent.createChooser(intent, "Share"));
//
   // }*/*/

    private void shareFacebook(){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Выберите свой букет на http://buket.club");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                break;
            }
        }
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

    // Returns the URI path to the Bitmap displayed in specified ImageView
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

    @Override
    protected int getCoordinatorViewId() {
        return R.id.social_coord_layout;
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }
}
