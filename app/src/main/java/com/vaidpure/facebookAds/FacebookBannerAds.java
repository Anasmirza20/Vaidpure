package com.vaidpure.facebookAds;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.vaidpure.R;

public class FacebookBannerAds {
    AdView adView;
    LinearLayout adContainer;
    private Context context;

    public FacebookBannerAds(LinearLayout adContainer,AdView adView, Context context) {
        this.adView=adView;
        this.context=context;
        this.adContainer=adContainer;
    }

    public void showAds(){
        adContainer.addView(adView);
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    AdListener adListener = new AdListener() {
        @Override
        public void onError(Ad ad, AdError adError) {
            // Ad error callback
            Toast.makeText(
                    context,
                    "Error: " + adError.getErrorMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onAdLoaded(Ad ad) {
            // Ad loaded callback
        }

        @Override
        public void onAdClicked(Ad ad) {
            // Ad clicked callback
        }

        @Override
        public void onLoggingImpression(Ad ad) {
            // Ad impression logged callback
        }
    };
}
