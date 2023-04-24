package com.example.challenge_squad_apps.ui.webclient.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET


interface VideoDeviceService {

    @GET ("video-devices")
    suspend fun videoDeviceService() : Response<ResponseBody>

}

interface AlarmDeviceService {

    @GET("alarm-centrals")
    suspend fun alarmDeviceService() : Response<ResponseBody>

}