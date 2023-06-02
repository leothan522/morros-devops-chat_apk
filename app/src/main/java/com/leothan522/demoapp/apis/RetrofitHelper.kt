package com.leothan522.demoapp.apis

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    fun getAndroid(): RetrofitInterface {

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Direcciones().BASE_URL_ANDROID)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RetrofitInterface::class.java)

        return retrofitBuilder
    }

    fun getLaravel(): RetrofitInterface {

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Direcciones().BASE_URL_LARAVEL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RetrofitInterface::class.java)

        return retrofitBuilder
    }
}