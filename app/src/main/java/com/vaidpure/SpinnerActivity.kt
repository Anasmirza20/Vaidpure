package com.vaidpure

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyInterstitial
import com.adcolony.sdk.AdColonyInterstitialListener
import com.adcolony.sdk.AdColonyZone
import com.vaidpure.databinding.ActivitySpinnerBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import rubikstudio.library.model.LuckyItem
import java.util.*

class SpinnerActivity : AppCompatActivity() {
    var data: MutableList<LuckyItem> = ArrayList()
    private val isFirstAd = true
/*    private var isFirstTime = true
    private var ad: AdColonyInterstitial? = null
    private var adColonyInterstitialListener: AdColonyInterstitialListener? = null
    private val APP_ID = "appdcb71b52e9be4747a7"
    private val ZONE_ID = "vzed957b59946c4d0c80"*/
    private var workingId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySpinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        workingId = intent.getStringExtra(Utils.WORKING_ID).toString()
//        adColonyAds()
        val luckyItem1 = LuckyItem()
        luckyItem1.topText = "1"
        luckyItem1.icon = R.drawable.test1
        luckyItem1.color = -0xc20
        data.add(luckyItem1)
        val luckyItem2 = LuckyItem()
        luckyItem2.topText = "2"
        luckyItem2.icon = R.drawable.test2
        luckyItem2.color = -0x1f4e
        data.add(luckyItem2)
        val luckyItem3 = LuckyItem()
        luckyItem3.topText = "3"
        luckyItem3.icon = R.drawable.test3
        luckyItem3.color = -0x3380
        data.add(luckyItem3)

        //////////////////
        val luckyItem4 = LuckyItem()
        luckyItem4.topText = "4"
        luckyItem4.icon = R.drawable.test4
        luckyItem4.color = -0xc20
        data.add(luckyItem4)
        val luckyItem5 = LuckyItem()
        luckyItem5.topText = "5"
        luckyItem5.icon = R.drawable.test5
        luckyItem5.color = -0x1f4e
        data.add(luckyItem5)
        val luckyItem6 = LuckyItem()
        luckyItem6.topText = "6"
        luckyItem6.icon = R.drawable.test6
        luckyItem6.color = -0x3380
        data.add(luckyItem6)
        //////////////////

        //////////////////////
        val luckyItem7 = LuckyItem()
        luckyItem7.topText = "7"
        luckyItem7.icon = R.drawable.test7
        luckyItem7.color = -0xc20
        data.add(luckyItem7)
        val luckyItem8 = LuckyItem()
        luckyItem8.topText = "8"
        luckyItem8.icon = R.drawable.test8
        luckyItem8.color = -0x1f4e
        data.add(luckyItem8)
        val luckyItem9 = LuckyItem()
        luckyItem9.topText = "9"
        luckyItem9.icon = R.drawable.test9
        luckyItem9.color = -0x3380
        data.add(luckyItem9)
        ////////////////////////
        val luckyItem10 = LuckyItem()
        luckyItem10.topText = "10"
        luckyItem10.icon = R.drawable.test10
        luckyItem10.color = -0x1f4e
        data.add(luckyItem10)
        val luckyItem11 = LuckyItem()
        luckyItem11.topText = "20"
        luckyItem11.icon = R.drawable.test10
        luckyItem11.color = -0x1f4e
        data.add(luckyItem11)
        val luckyItem12 = LuckyItem()
        luckyItem12.topText = "30"
        luckyItem12.icon = R.drawable.test10
        luckyItem12.color = -0x1f4e
        data.add(luckyItem12)
        /////////////////////
        binding.luckyWheel.setData(data)
        binding.luckyWheel.setRound(5)
        binding.luckyWheel.isTouchEnabled = false
        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/binding.play.setOnClickListener {
            val index = 0
            binding.play.isEnabled = false
            binding.luckyWheel.startLuckyWheelWithTargetIndex(index)
        }
        binding.luckyWheel.setLuckyRoundItemSelectedListener { index: Int ->
//            AdColony.requestInterstitial(ZONE_ID, adColonyInterstitialListener!!)
            val hashMap = HashMap<String, RequestBody>()
            hashMap[Utils.WORKING_TYPE] = Utils.SPIN_WHEEL.toRequestBody("text/plain".toMediaTypeOrNull())
            hashMap[Utils.WORKING_ID] = workingId.toRequestBody("text/plain".toMediaTypeOrNull())
            PostJobResult.postResult(hashMap)
            Handler(Looper.getMainLooper()).postDelayed({ onBackPressed() }, 500)
            Toast.makeText(applicationContext, data[index].topText, Toast.LENGTH_SHORT).show()
        }
    }

    private val randomIndex: Int
        get() {
            val rand = Random()
            return rand.nextInt(data.size - 1)
        }
    private val randomRound: Int
        get() {
            val rand = Random()
            return rand.nextInt(10) + 15
        }

   /* override fun onResume() {
        super.onResume()
        if (ad == null || ad!!.isExpired) {
            if (isFirstTime) {
                isFirstTime = false;
                adColonyInterstitialListener?.let { AdColony.requestInterstitial(ZONE_ID, it) };
            }
        }
    }*/

   /* private fun adColonyAds() {
        adColonyInterstitialListener = object : AdColonyInterstitialListener() {
            override fun onRequestFilled(adColonyInterstitial: AdColonyInterstitial) {
                ad = adColonyInterstitial
                //                Toast.makeText(SpinnerActivity.this, "Add Filled", Toast.LENGTH_SHORT).show();
                ad!!.show()
            }

            override fun onRequestNotFilled(zone: AdColonyZone) {
                super.onRequestNotFilled(zone)
                Toast.makeText(this@SpinnerActivity, "onRequestNotFilled", Toast.LENGTH_SHORT).show()
                AdColony.requestInterstitial(ZONE_ID, adColonyInterstitialListener!!)
            }

            override fun onOpened(ad: AdColonyInterstitial) {
//                Toast.makeText(SpinnerActivity.this, "onOpened", Toast.LENGTH_SHORT).show();
            }



            override fun onExpiring(ad: AdColonyInterstitial) {
                AdColony.requestInterstitial(ZONE_ID, this)
                //                Toast.makeText(SpinnerActivity.this, "onExpiring", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}