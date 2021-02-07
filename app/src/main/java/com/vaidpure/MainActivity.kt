package com.vaidpure

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adcolony.sdk.*
import com.facebook.ads.AdSettings
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.vaidpure.Utils.*
import com.vaidpure.adMobsAds.AdMobsBannerAd
import com.vaidpure.adMobsAds.AdMobsInterstitialAds
import com.vaidpure.adMobsAds.AdMobsRewardedAd
import com.vaidpure.databinding.ActivityMainBinding
import com.vaidpure.facebookAds.FacebookBannerAds
import com.vaidpure.facebookAds.FacebookInterstitialAdsClass
import com.vaidpure.facebookAds.FacebookRewardedVideoAd
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val isFirstTime = true
    private var ad: AdColonyInterstitial? = null
    private var adColonyInterstitialListener: AdColonyInterstitialListener? = null
    private val APP_ID = "appdcb71b52e9be4747a7"
    private val ZONE_ID = "vzed957b59946c4d0c80"
    private var binding: ActivityMainBinding? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var url = "http://vaidpurecash.in/include/"
    private val PERMISSIONS_REQUEST_ENABLE_GPS = 104
    private val REQUEST_PERMISSION_CODE = 105
    private var Latitude = 0.0
    private var Longitude = 0.0
    private var currentLocation: Location? = null
    private var fbAdView: AdView? = null
    private var userId: String? = ""
    private var appPackageName: String? = ""
    private var redirectCode: String? = ""
    private var typeNo: String = ""
    private var imgUser: String = ""
    private var imgType: String = ""
    private var workingId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        /*AudienceNetworkAds.initialize(this)
        adColonyAds()
        val options = AdColonyAppOptions().setKeepScreenOn(true)
                .setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true)
                .setPrivacyConsentString(AdColonyAppOptions.GDPR, "1")
        val APP_ID = "appdcb71b52e9be4747a7"
        val ZONE_ID = "vzed957b59946c4d0c80"
        AdColony.configure(this, options, APP_ID, ZONE_ID)*/
        initializeLocationListener()
        submit()
        requestAppUpdate()
/*        MobileAds.initialize(this) { }
        AudienceNetworkAds.initialize(this)
        AdSettings.setTestMode(true)
        showInterstitialAdMobsAds()
        //        showFbInterstitialAds();
        showAdMobsBannerAd()
        //        createAndLoadRewardedAd();
//        showFbBannerAds();
        showFbRewardedAd()
        showAdMobsRewardedAds()*/
    }

    /*    private void showAdsAfterSomeTime() {
        new Handler().postDelayed(() -> {
            Toast.makeText(this, "showAds", Toast.LENGTH_SHORT).show();
            showAds();
            showAdsAfterSomeTime();
        }, 1000 * 60);
    }*/
    @SuppressLint("SetJavaScriptEnabled")
    @JavascriptInterface
    fun submit() {
        binding!!.webview.webViewClient = object : CustomWebViewClient(binding!!.image) {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val uri = Uri.parse(url)
                if (url.contains("http://vaidpurecash.in/include/dashboard/index.php") && url.contains(DEVICE_USER)) {
                    userId = uri.getQueryParameter(DEVICE_USER)
                    callDeviceIdApi()
                } else if (url.contains("http://vaidpurecash.in/include/signup.php") && !url.contains(DEVICE_ID)) {
                    callDeviceIdOnSignUp()
                } else if (url.contains(JOB_ID) && url.contains(WORKING_ID)) {
                    typeNo = uri.getQueryParameter(JOB_ID).toString()
                    workingId = uri.getQueryParameter(WORKING_ID).toString()
                    when (typeNo) {
                        SCRATCH -> {
                            startActivity(Intent(this@MainActivity, ScratchCardActivity::class.java).putExtra(WORKING_ID, workingId))
                        }
                        SPIN_WHEEL -> {
                            startActivity(Intent(this@MainActivity, SpinnerActivity::class.java).putExtra(WORKING_ID, workingId))
                        }
                       /* VIDEO_WALL -> {
                            if (ad == null || ad!!.isExpired) AdColony.requestInterstitial(ZONE_ID, (adColonyInterstitialListener)!!)
                            val hashMap = HashMap<String, RequestBody>()
                            hashMap[WORKING_TYPE] = VIDEO_WALL.toRequestBody("text/plain".toMediaTypeOrNull())
                            hashMap[WORKING_ID] = workingId.toRequestBody("text/plain".toMediaTypeOrNull())
                            PostJobResult.postResult(hashMap)
                        }*/
                    }
                } else if (url.contains(ERROR_ID)) {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setPositiveButton("Okay", DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                    builder.setMessage("Your daily limit has been reached")
                            .setTitle("Alert!")
                    val dialog = builder.create()
                    dialog.show()
                } else if (url.contains(TYPE_NO) && url.contains(IMG_USER) && url.contains(IMG_TYPE)) {
                    //http://vaidpurecash.in/include/dashboard/index.php?job-id=SpinWheel
                    typeNo = uri.getQueryParameter(TYPE_NO).toString()
                    imgUser = uri.getQueryParameter(IMG_USER).toString()
                    imgType = uri.getQueryParameter(IMG_TYPE).toString()
                    if (permissions) openGallery()
                } else if (url.contains(TASK_USER) && url.contains(PACKAGE_NAME) && url.contains(REDIRECT_CODE)) {
                    userId = uri.getQueryParameter(TASK_USER)
                    appPackageName = uri.getQueryParameter(PACKAGE_NAME)
                    redirectCode = uri.getQueryParameter(REDIRECT_CODE)
                    redirectUrl
                    return true
                } else if (url.contains("referral-id")) {
                    openBrowser(url)
                    return true
                } else if (url.contains("vaidpurecash")) {
                    return false
                } else if (Objects.requireNonNull(Uri.parse(url).scheme).equals("market", ignoreCase = true)) {
                    return try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        val host = view.context as Activity
                        host.startActivity(intent)
                        true
                    } catch (e: ActivityNotFoundException) {
                        // Google Play app is not installed, you may want to open the app store link
                        view.loadUrl("https://play.app.goo.gl/?link=https://play.google.com/store/apps/" + uri.host + "?" + uri.query)
                        false
                    }
                } else if ((url.startsWith("http:")
                                || url.startsWith("https:"))) {
                    openBrowser(url)
                    return true
                }
                return false
            }
        }
        binding?.webview?.apply {
            settings.apply {
                javaScriptEnabled = true
                @Suppress("DEPRECATION")
                setAppCacheEnabled(true)
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
                builtInZoomControls = true
                displayZoomControls = false
            }
            overScrollMode = WebView.OVER_SCROLL_NEVER
//            this@MainActivity.url = url?.trim { it <= ' ' }?.replace("\\s".toRegex(), "%20").toString()
            loadUrl(this@MainActivity.url)
        }


/*        binding.webview.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                MainActivity.this.mUploadMessage = filePathCallback;
                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "TestApp");
                File file = new File(imageStorageDir + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg");
                MainActivity.this.imageUri = Uri.fromFile(file); // save to the private variable

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/ *");

                Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                startActivityForResult(chooserIntent, 100);
                return false;
            }
        });*/binding!!.webview.setDownloadListener { url: String?, _: String?, _: String?, _: String?, _: Long ->
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            @Suppress("DEPRECATION")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,  //Download folder
                    "download") //Name of file
            val dm: DownloadManager = getSystemService(
                    DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    //Creating image file for upload
    @Throws(IOException::class)
    private fun create_image(): File {
        @SuppressLint("SimpleDateFormat") val file_name = SimpleDateFormat("yyyy_mm_ss").format(Date())
        val new_name = "file_" + file_name + "_"
        val sd_directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(new_name, ".jpg", sd_directory)
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (binding!!.webview.canGoBack()) {
                    binding!!.webview.goBack()
                } else {
                    finish()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }// start location updates to get the current lat long// Logic to handle location object// Got last known location. In some rare situations this can be null.

    // get last location of the user
    private val deviceLocation: Unit
        get() {
            // get last location of the user
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            fusedLocationClient!!.lastLocation
                    .addOnSuccessListener(this) { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Latitude = location.getLatitude()
                            Longitude = location.getLongitude()
                        } else {
                            // start location updates to get the current lat long
                            startLocationUpdates()
                        }
                    }
        }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient!!.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper())
    }

    private fun initializeLocationListener() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest?.setInterval(1000)
        locationRequest?.setFastestInterval(5000)
        locationRequest?.setNumUpdates(1)
        onLocationCallback()
    }

    private fun onLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    currentLocation = location
                }
                Latitude = currentLocation!!.latitude
                Longitude = currentLocation!!.longitude
            }
        }
    }

    // method to check that the GPs is enable or not
    private val isMapsEnabled: Boolean
        get() {
            val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
                return false
            }
            return true
        }

    /*            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
   
               } else {*/
    //            }
    private val permissions: Boolean
        get() = if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (isMapsEnabled) {
                deviceLocation
            }
            true
        } else {
/*            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {*/
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
            //            }
            false
        }

    private fun buildAlertMessageNoGps() {
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(this) { obj: LocationSettingsResponse -> obj.locationSettingsStates }
        task.addOnFailureListener(this) { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@MainActivity,
                            PERMISSIONS_REQUEST_ENABLE_GPS)
                } catch (sendEx: SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                }
                if (isMapsEnabled) {
                    deviceLocation
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //        binding.adView.resume();
        checkAppInstall()
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
            }
        }
    }

    public override fun onPause() {
        // Cancel the timer if the game is paused.
//        binding.adView.pause();
        super.onPause()
    }

    override fun onDestroy() {
/*        binding.adView.destroy();
        fbAdView.destroy();*/
        super.onDestroy()
    }

    // Ads Code
    private fun showFbInterstitialAds() {
        val facebookInterstitialAdsClass = FacebookInterstitialAdsClass(this)
        facebookInterstitialAdsClass.setFbInterstitialAdsSetup()
    }

    private fun showFbBannerAds() {
        fbAdView = AdView(this, getString(R.string.fb_banner_placement_id), AdSize.BANNER_HEIGHT_50)
        val facebookBannerAds = FacebookBannerAds(binding!!.bannerContainer, fbAdView, this)
        facebookBannerAds.showAds()
    }

    private fun showFbRewardedAd() {
        val facebookRewardedVideoAd = FacebookRewardedVideoAd(this)
        facebookRewardedVideoAd.setupAd()
    }

    private fun showInterstitialAdMobsAds() {
        val adMobsInterstitialAds = AdMobsInterstitialAds(this)
        adMobsInterstitialAds.showInterstitialAds()
    }

    private fun showAdMobsBannerAd() {
        val adMobsBannerAd = AdMobsBannerAd(binding!!.adView, this)
        adMobsBannerAd.showBannerAd()
    }

    private fun showAdMobsRewardedAds() {
        val adMobsRewardedAd = AdMobsRewardedAd(this, this)
        adMobsRewardedAd.createAndLoadRewardedAd()
    }

    private fun checkAppInstall() {
        if (appPackageName!!.isNotEmpty() && userId!!.isNotEmpty()) {
            val isAppInstalled = isPackageInstalled(appPackageName, packageManager)
            if (isAppInstalled) {
                val apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
                val call = apiInterface.postPackageStatus(userId!!.trim { it <= ' ' }, appPackageName!!.trim { it <= ' ' })
                call?.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(this@MainActivity, "Task Completed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private val redirectUrl: Unit
        get() {
            if (appPackageName!!.isNotEmpty() && userId!!.isNotEmpty() && redirectCode!!.isNotEmpty()) {
                val apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
                val call = apiInterface.getRedirectUrl(redirectCode!!.toInt())
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful && response.body() != null) {
                            try {
                                val resp = response.body()!!.string().split(" ").toTypedArray()
                                val url = resp[resp.size - 1]
                                openBrowser(url)
                            } catch (e: IOException) {
                                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    private fun requestAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateManager!!.registerListener(listener)
        checkUpdates()
    }

    private fun checkUpdates() {
        appUpdateInfoTask!!.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            this,
                            MY_REQUEST_CODE)
                } catch (ignored: SendIntentException) {
                }
            }
        }
    }

    private var appUpdateInfoTask: Task<AppUpdateInfo>? = null
    private var appUpdateManager: AppUpdateManager? = null

    // Create a listener to track request state updates.
    var listener = InstallStateUpdatedListener { state: InstallState ->
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
//            val bytesDownloaded = state.bytesDownloaded()
//            val totalBytesToDownload = state.totalBytesToDownload()
//            // Implement progress bar.
        }
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()
        }
    }

    // Before starting an update, register a listener for updates.
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                // If the update is cancelled or fails,
                // you can request to start the update again.
                checkUpdates()
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (data != null) {
                val image = data.data
                if (image != null) {
                    val path = ImageFilePath.getPath(this, image)
                    val part: MultipartBody.Part = convertIntoMultipart(path)
                    val imgUsr: RequestBody = imgUser.toRequestBody("text/plain".toMediaTypeOrNull())
                    val type: RequestBody = imgType.toRequestBody("text/plain".toMediaTypeOrNull())
                    val typeNumber: RequestBody = typeNo.toRequestBody("text/plain".toMediaTypeOrNull())
                    val bodyMap: MutableMap<String?, RequestBody?> = HashMap()
                    bodyMap[IMG_USER] = imgUsr
                    bodyMap[IMG_TYPE] = type
                    bodyMap[TYPE_NO] = typeNumber
                    uploadDocs(part, bodyMap)
                }
            } else {
                Toast.makeText(this, "Upload Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(
                binding!!.root,
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("RESTART") { appUpdateManager!!.completeUpdate() }
        snackbar.setActionTextColor(
                ContextCompat.getColor(this, R.color.colorPrimaryDark))
        snackbar.show()
    }

    private fun uploadDocs(part: MultipartBody.Part?, map: Map<String?, RequestBody?>) {
        val apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = apiInterface.uploadDocs(map, part)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        Toast.makeText(this@MainActivity, response.body()!!.string(), Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun adColonyAds() {
        adColonyInterstitialListener = object : AdColonyInterstitialListener() {
            override fun onRequestFilled(adColonyInterstitial: AdColonyInterstitial) {
                ad = adColonyInterstitial
                //                Toast.makeText(MainActivity.this, "Add Filled", Toast.LENGTH_SHORT).show();
                ad!!.show()
            }

            override fun onRequestNotFilled(zone: AdColonyZone) {
                super.onRequestNotFilled(zone)
                Toast.makeText(this@MainActivity, "onRequestNotFilled", Toast.LENGTH_SHORT).show()
                AdColony.requestInterstitial(ZONE_ID, adColonyInterstitialListener!!)
            }

            override fun onOpened(ad: AdColonyInterstitial) {
//                Toast.makeText(MainActivity.this, "onOpened", Toast.LENGTH_SHORT).show();
            }

            override fun onExpiring(ad: AdColonyInterstitial) {
                AdColony.requestInterstitial(ZONE_ID, this)
                //                Toast.makeText(MainActivity.this, "onExpiring", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun callDeviceIdApi() {
        val id = Settings.Secure.getString(contentResolver,
                Settings.Secure.ANDROID_ID);
        val map = HashMap<String?, RequestBody?>()
        map[DEVICE_USER] = userId?.toRequestBody("text/plain".toMediaTypeOrNull())
        map[DEVICE_ID] = id.toRequestBody("text/plain".toMediaTypeOrNull())
        val apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = apiInterface.postDeviceToken(map)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.i(TAG, "onResponse: " + response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    @SuppressLint("HardwareIds")
    private fun callDeviceIdOnSignUp() {
        val deviceId = Settings.Secure.getString(contentResolver,
                Settings.Secure.ANDROID_ID).toRequestBody("text/plain".toMediaTypeOrNull())
        val apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = apiInterface.postDeviceIdOnSignUp(deviceId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val url = response.body()?.string()?.trim()!!
                binding?.webview?.loadUrl(url)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val MY_REQUEST_CODE = 100
        private const val REQUEST_IMAGE_GALLERY = 102
        const val TASK_USER = "task-user"
        const val PACKAGE_NAME = "package-name"
        const val REDIRECT_CODE = "redirect-code"
    }
}