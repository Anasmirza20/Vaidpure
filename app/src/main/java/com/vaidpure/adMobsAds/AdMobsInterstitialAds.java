package com.vaidpure.adMobsAds;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.vaidpure.R;

public class AdMobsInterstitialAds {
    private InterstitialAd mInterstitialAd;

    public AdMobsInterstitialAds(Context context) {
        this.context = context;
    }

    private Context context;

    public void showInterstitialAds(){
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.ad_mobs_interstitial_ad_key));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                showAdWithDelay();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
//                Toast.makeText(context, "onAdFailedToLoad() with error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                showInterstitialAds();
            }
        });
    }


    private void showAdWithDelay() {
        new Handler().postDelayed(() -> {
            // Check if interstitialAd has been loaded successfully
            if (mInterstitialAd == null || !mInterstitialAd.isLoaded()) {
                return;
            }
            // Show the ad
            mInterstitialAd.show();
        }, 1000 * 30);
    }
}
