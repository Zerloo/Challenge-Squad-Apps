package com.example.challenge_squad_apps.webclient.services

import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.models.AddDevice
import com.example.challenge_squad_apps.webclient.models.EditDevice
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface VideoDeviceService {

    @GET("video-devices")
    suspend fun videoDeviceService(): Response<ResponseBody>

    @GET("video-devices/{id}")
    suspend fun byIdVideoDeviceService(@Path("id") id: String): Response<ResponseBody>

    @PATCH("video-devices/{id}")
    suspend fun patchVideoDevice(
        @Path("id") id: String,
        @Body device: EditDevice
    ): Response<ResponseBody>

    @POST("video-devices")
    suspend fun postVideoDevice(
        @Body device: AddDevice
    ): Response<ResponseBody>

    @DELETE("video-devices/{id}")
    suspend fun deleteVideoDeviceService(@Path("id") id: String): Response<ResponseBody>

}

interface AlarmDeviceService {

    @GET("alarm-centrals")
    suspend fun alarmDeviceService(): Response<ResponseBody>

    @GET("alarm-centrals/{id}")
    suspend fun byIdVideoDeviceService(@Path("id") id: String): Response<ResponseBody>

    @PATCH("alarm-centrals/{id}")
    suspend fun patchAlarmDevice(
        @Path("id") id: String,
        @Body device: EditDevice
    ): Response<ResponseBody>

    @POST("alarm-centrals")
    suspend fun postAlarmDevice(
        @Body device: AddDevice
    ): Response<ResponseBody>

    @DELETE("alarm-centrals/{id}")
    suspend fun deleteAlarmDeviceService(@Path("id") id: String): Response<ResponseBody>
}