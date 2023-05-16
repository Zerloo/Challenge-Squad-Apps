package com.example.challenge_squad_apps.webclient.services

import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.EditDevice
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AlarmDeviceService {

    @GET("alarm-centrals")
    suspend fun alarmDeviceService(): Response<ResponseBody>

    @PATCH("alarm-centrals/{id}")
    suspend fun patchAlarmDevice(
        @Path("id") id: String,
        @Body device: EditDevice
    ): Response<ResponseBody>

    @POST("alarm-centrals")
    suspend fun postAlarmDevice(
        @Body device: AlarmDevice
    ): Response<ResponseBody>

    @DELETE("alarm-centrals/{id}")
    suspend fun deleteAlarmDeviceService(@Path("id") id: String): Response<ResponseBody>
}