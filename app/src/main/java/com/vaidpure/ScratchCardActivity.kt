package com.vaidpure

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyInterstitial
import com.adcolony.sdk.AdColonyInterstitialListener
import com.adcolony.sdk.AdColonyZone
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.vaidpure.Utils.*
import com.vaidpure.databinding.ActivityScratchCardBinding
import com.vaidpure.facebookAds.FacebookBannerAds
import dev.skymansandy.scratchcardlayout.listener.ScratchListener
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class ScratchCardActivity : AppCompatActivity(), ScratchListener {
    private var isFirstAd: Boolean = true
    private var isFirstTime: Boolean = true
    private var ad: AdColonyInterstitial? = null
    private var fbAdView: AdView? = null
    private var fbAdView2: AdView? = null
    private var fbAdView3: AdView? = null
    private var fbAdView4: AdView? = null
    private var fbAdView5: AdView? = null
    private var adColonyInterstitialListener: AdColonyInterstitialListener? = null
    private val APP_ID = "appdcb71b52e9be4747a7"
    private val ZONE_ID = "vzed957b59946c4d0c80"
    private var workingId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScratchCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        workingId = intent.getStringExtra(WORKING_ID).toString()
        resetLibraryView()
/*        adColonyAds()
        showFbBannerAds()
        showAdMobsBannerAd2()
        showAdMobsBannerAd3()
        showAdMobsBannerAd4()
        showAdMobsBannerAd5()*/
    }

    override fun onDestroy() {
        super.onDestroy()
        fbAdView?.destroy()
    }


    private fun showFbBannerAds() {
        fbAdView = AdView(this, getString(R.string.fb_banner_placement_id), AdSize.BANNER_HEIGHT_50)
        val facebookBannerAds = FacebookBannerAds(binding.adView, fbAdView, this)
        facebookBannerAds.showAds()
    }

    private lateinit var binding: ActivityScratchCardBinding


    private fun showAdMobsBannerAd2() {
        fbAdView2 = AdView(this, getString(R.string.fb_banner_placement_id), AdSize.BANNER_HEIGHT_50)
        val facebookBannerAds = FacebookBannerAds(binding.adView2, fbAdView2, this)
        facebookBannerAds.showAds()
    }

    private fun showAdMobsBannerAd3() {
        fbAdView3 = AdView(this, getString(R.string.fb_banner_placement_id), AdSize.BANNER_HEIGHT_50)
        val facebookBannerAds = FacebookBannerAds(binding.adView3, fbAdView3, this)
        facebookBannerAds.showAds()
    }

    private fun showAdMobsBannerAd4() {
        fbAdView4 = AdView(this, getString(R.string.fb_banner_placement_id), AdSize.BANNER_HEIGHT_50)
        val facebookBannerAds = FacebookBannerAds(binding.adView4, fbAdView4, this)
        facebookBannerAds.showAds()
    }

    private fun showAdMobsBannerAd5() {
        fbAdView5 = AdView(this, getString(R.string.fb_banner_placement_id), AdSize.BANNER_HEIGHT_50)
        val facebookBannerAds = FacebookBannerAds(binding.adView5, fbAdView5, this)
        facebookBannerAds.showAds()
    }

    private fun resetLibraryView() {
        binding.scratchCard.setScratchListener(this)
        binding.scratchCard.setRevealFullAtPercent(50)
        binding.scratchCard2.setScratchListener(this)
        binding.scratchCard2.setRevealFullAtPercent(50)
        binding.scratchCard3.setScratchListener(this)
        binding.scratchCard3.setRevealFullAtPercent(50)
        binding.scratchCard4.setScratchListener(this)
        binding.scratchCard4.setRevealFullAtPercent(50)
        binding.scratchCard5.setScratchListener(this)
        binding.scratchCard5.setRevealFullAtPercent(50)
    }

    override fun onScratchStarted() {
        binding.scrollView.setScrollingEnabled(false)
        Log.d("TAG", "Scratch started")
    }

    override fun onScratchProgress(scratchCardLayout: ScratchCardLayout, atLeastScratchedPercent: Int) {
        binding.scrollView.setScrollingEnabled(false)
        Log.d("TAG", "Progress = $atLeastScratchedPercent")
    }

    override fun onScratchComplete() {
   /*     adColonyInterstitialListener?.let {
            isFirstAd = true
            AdColony.requestInterstitial(ZONE_ID, it)
        }*/
        val hashMap = HashMap<String, RequestBody>()
        hashMap[WORKING_TYPE] = SCRATCH.toRequestBody("text/plain".toMediaTypeOrNull())
        hashMap[WORKING_ID] = workingId.toRequestBody("text/plain".toMediaTypeOrNull())
        PostJobResult.postResult(hashMap)
        binding.scrollView.setScrollingEnabled(true)
        Log.d("Tag", "Scratch ended")
    }

/*
    override fun onResume() {
        super.onResume()
        if (ad == null || ad!!.isExpired) {
            // Optionally update location info in the ad options for each request:
            // LocationManager locationManager =
            //     (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Location location =
            //     locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // adOptions.setUserMetadata(adOptions.getUserMetadata().setUserLocation(location));
            if (isFirstTime) {
                isFirstTime = false
                adColonyInterstitialListener?.let { AdColony.requestInterstitial(ZONE_ID, it) }
            }
        }
    }
*/


    private fun adColonyAds() {
        adColonyInterstitialListener = object : AdColonyInterstitialListener() {
            override fun onRequestFilled(ad: AdColonyInterstitial) {
                // Ad passed back in request filled callback, ad can now be shown
                this@ScratchCardActivity.ad = ad
                Toast.makeText(this@ScratchCardActivity, "Add Filled", Toast.LENGTH_SHORT).show()
                if (isFirstAd) {
                    isFirstAd = false
                    ad.show()
                }
            }

            override fun onRequestNotFilled(zone: AdColonyZone) {
                // Ad request was not filled
                Toast.makeText(this@ScratchCardActivity, "onRequestNotFilled", Toast.LENGTH_SHORT).show()
                adColonyInterstitialListener?.let { AdColony.requestInterstitial(ZONE_ID, it) };
                Log.d("TAG", "onRequestNotFilled")
            }

            override fun onOpened(ad: AdColonyInterstitial) {
                // Ad opened, reset UI to reflect state change
                Toast.makeText(this@ScratchCardActivity, "onOpened", Toast.LENGTH_SHORT).show()
            }

            override fun onExpiring(ad: AdColonyInterstitial) {
                // Request a new ad if ad is expiring
                AdColony.requestInterstitial(ZONE_ID, this)
                Toast.makeText(this@ScratchCardActivity, "onExpiring", Toast.LENGTH_SHORT).show()
            }
        }
    }

}