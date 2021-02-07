package com.vaidpure.facebookAds;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.vaidpure.R;

public class FacebookRewardedVideoAd {

    public FacebookRewardedVideoAd(Context context) {
        this.context = context;
    }

    private Context context;
    private static final String TAG = FacebookRewardedVideoAd.class.getSimpleName();
    private RewardedVideoAd rewardedVideoAd;

    public void setupAd() {
        rewardedVideoAd = new RewardedVideoAd(context, context.getString(R.string.fb_reward_placement_id));

        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoCompleted() {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onRewardedVideoClosed() {
                setupAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
//                Toast.makeText(context,adError.getErrorMessage() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                showAdWithDelay();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        };

        rewardedVideoAd.buildLoadAdConfig().withAdListener(rewardedVideoAdListener).build();
        rewardedVideoAd.loadAd();

    }

    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        // Handler handler = new Handler();
        new Handler().postDelayed(() -> {
            // Check if rewardedVideoAd has been loaded successfully
            if (rewardedVideoAd == null || !rewardedVideoAd.isAdLoaded()) {
                return;
            }
            // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
            if (rewardedVideoAd.isAdInvalidated()) {

                return;
            }
            rewardedVideoAd.show();
        }, 1000 * 60 * 30); // Show the ad after 15 minutes
    }
}