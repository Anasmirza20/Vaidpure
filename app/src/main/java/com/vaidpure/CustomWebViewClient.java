package com.vaidpure;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class CustomWebViewClient extends WebViewClient {
    private ImageView imageView;
    public CustomWebViewClient(ImageView imageView){
        this.imageView=imageView;
    }
    @Override
    public void onPageStarted(WebView webview, String url, Bitmap favicon) {
//            if (ShowOrHideWebViewInitialUse.equals("show")) {
        webview.setVisibility(View.INVISIBLE);
//            }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        super.onPageFinished(view, url);
    }
}