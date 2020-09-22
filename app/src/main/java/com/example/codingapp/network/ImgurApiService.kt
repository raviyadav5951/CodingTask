package com.example.codingapp.network

import com.example.codingapp.model.ImageResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * This class can also be termed as Repository in MVVM terminology
 */
private const val BASE_URL="https://api.imgur.com"
private const val REQUEST_TIMEOUT_DURATION = 10
private const val DEBUG = true
private const val AUTH_HEADER="Client-ID 137cda6b5008a7c";

class ImgurApiService {

    private val api=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(createRequestInterceptorClient())
        .build()
        .create(ImgurApi::class.java)

    fun getImages(searchInput:String?):Single<ImageResponse> = api.getImagesFromImgur(searchInput)

    //Here we have added the interceptor to display logs when api is called.
    private fun createRequestInterceptorClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.addHeader("Authorization",AUTH_HEADER)
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return if (DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                .build()
        }
    }
}