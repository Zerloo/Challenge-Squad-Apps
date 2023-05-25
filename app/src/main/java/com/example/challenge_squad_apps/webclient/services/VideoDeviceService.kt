package com.example.challenge_squad_apps.webclient.services

import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.EditDevice
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
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
    fun videoDeviceService(): Single<Response<ResponseBody>>

    @PATCH("video-devices/{id}")
    fun patchVideoDevice(
        @Path("id") id: String,
        @Body device: EditDevice
    ): Single<Response<ResponseBody>>

    @POST("video-devices")
    fun postVideoDevice(
        @Body device: VideoDevice
    ): Single<Response<ResponseBody>>

    @DELETE("video-devices/{id}")
    fun deleteVideoDeviceService(@Path("id") id: String): Single<Response<ResponseBody>>
}

