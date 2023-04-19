package com.example.challenge_squad_apps.webclient

import com.example.challenge_squad_apps.webclient.services.AlarmDeviceService
import com.example.challenge_squad_apps.webclient.services.VideoDeviceService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class retrofitInitialization(token: String) {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.100.11.117:3001/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val videoDeviceService: VideoDeviceService = retrofit.create(VideoDeviceService::class.java)
    val alarmDeviceService: AlarmDeviceService = retrofit.create(AlarmDeviceService::class.java)

}
