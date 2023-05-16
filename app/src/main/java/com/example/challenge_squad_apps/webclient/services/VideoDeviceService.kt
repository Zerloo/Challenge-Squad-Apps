package com.example.challenge_squad_apps.webclient.services

import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.EditDevice
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
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

    @PATCH("video-devices/{id}")
    suspend fun patchVideoDevice(
        @Path("id") id: String,
        @Body device: EditDevice
    ): Response<ResponseBody>

    @POST("video-devices")
    suspend fun postVideoDevice(
        @Body device: VideoDevice
    ): Response<ResponseBody>

    @DELETE("video-devices/{id}")
    suspend fun deleteVideoDeviceService(@Path("id") id: String): Response<ResponseBody>
}

