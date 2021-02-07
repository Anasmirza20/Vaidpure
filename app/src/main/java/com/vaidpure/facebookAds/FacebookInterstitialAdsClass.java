package com.vaidpure.facebookAds;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.vaidpure.R;

public class FacebookInterstitialAdsClass {

    public FacebookInterstitialAdsClass(Context context) {
        this.context = context;
    }

    private Context context;


    InterstitialAd interstitialAdFacebook;

    public void setFbInterstitialAdsSetup() {
        interstitialAdFacebook = new InterstitialAd(context, context.getString(R.string.fb_interstitial_placement_id));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                setFbInterstitialAdsSetup();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
//                Toast.makeText(context, adError.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                setFbInterstitialAdsSetup();
            }

            @Override
            public void onAdLoaded(Ad ad) {
//                Toast.makeText(context, "Fb Ad Loaded", Toast.LENGTH_SHORT).show();
                showAdWithDelay();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        // Set listeners for the Interstitial Ad
       InterstitialAd.InterstitialLoadAdConfig loadAdConfig = interstitialAdFacebook.buildLoadAdConfig().withAdListener(interstitialAdListener).build();
        interstitialAdFacebook.loadAd(loadAdConfig);
    }

    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        // Handler handler = new Handler();
        new Handler().postDelayed(() -> {
            // Check if interstitialAd has been loaded successfully
            if (interstitialAdFacebook == null || !interstitialAdFacebook.isAdLoaded()) {
                return;
            }
            // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
            if (interstitialAdFacebook.isAdInvalidated()) {
                return;
            }
            // Show the ad
            interstitialAdFacebook.show();
        }, 1000 * 30*15); // Show the ad after 15 minutes
    }
}
