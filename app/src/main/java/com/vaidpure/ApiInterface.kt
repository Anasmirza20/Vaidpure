package com.vaidpure

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET(URL)
    fun postPackageStatus(@Query(MainActivity.TASK_USER) userId: String?, @Query(MainActivity.PACKAGE_NAME) packageName: String?): Call<ResponseBody?>?

    @GET(URL)
    fun getRedirectUrl(@Query(MainActivity.REDIRECT_CODE) packageName: Int): Call<ResponseBody>

//    @POST("http://vaidpurecash.in/include/signup.php")
    @POST(URL)
    @Multipart
    fun postDeviceIdOnSignUp(@Part("token") id: RequestBody?): Call<ResponseBody>

    @POST(URL)
    @Multipart
    fun postDeviceToken(@PartMap hashMap: @JvmSuppressWildcards Map<String?, RequestBody?>?): Call<ResponseBody>

    @POST(URL)
    @Multipart
    fun postJobResult(@PartMap hashMap: @JvmSuppressWildcards Map<String, RequestBody>): Call<ResponseBody>

    @Multipart
    @POST(URL)
    fun uploadDocs(
            @PartMap partMap: @JvmSuppressWildcards Map<String?, RequestBody?>?,
            @Part file: MultipartBody.Part?): Call<ResponseBody?>?

    companion object {
        const val URL = "task_api.php"
    }
}