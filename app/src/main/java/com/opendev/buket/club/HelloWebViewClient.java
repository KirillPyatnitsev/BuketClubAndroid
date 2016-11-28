package com.opendev.buket.club;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class HelloWebViewClient extends WebViewClient {

    Context context;
    public HelloWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http://goog")){
            Toast.makeText(context, "Успешно епта", Toast.LENGTH_SHORT).show();
        }
        view.loadUrl(url);
        return true;
    }
}