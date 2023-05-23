package com.example.challenge_squad_apps.webclient

import com.example.challenge_squad_apps.webclient.services.AlarmDeviceService
import com.example.challenge_squad_apps.webclient.services.VideoDeviceService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInitialization(token: String) {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    val gson : Gson = GsonBuilder()
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val videoDeviceService: VideoDeviceService = retrofit.create(VideoDeviceService::class.java)
    val alarmDeviceService: AlarmDeviceService = retrofit.create(AlarmDeviceService::class.java)
}
