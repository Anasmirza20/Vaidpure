package com.vaidpure.adMobsAds;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.vaidpure.R;

public class AdMobsRewardedAd {

    private RewardedAd rewardedAd;
    private Context context;

    public AdMobsRewardedAd(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private Activity activity;


    public RewardedAd createAndLoadRewardedAd() {
        rewardedAd = new RewardedAd(context,
                context.getString(R.string.ad_mobs_rewarded_ad_key));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Toast.makeText(context, "Ad Loaded", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> showAds(), 1000 * 70);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
//                Toast.makeText(context, "Load error " + errorCode, Toast.LENGTH_SHORT).show();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    private void showAds() {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    rewardedAd = createAndLoadRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display.
                }
            };
            rewardedAd.show(activity, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }
    }
}
