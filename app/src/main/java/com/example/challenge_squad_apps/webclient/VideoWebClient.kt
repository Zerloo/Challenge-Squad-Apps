package com.example.challenge_squad_apps.webclient

import com.example.challenge_squad_apps.webclient.models.VideoDevice
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class VideoWebClient {
    private val token: String =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjZlOWFkM2E2LWQyYTUtNDg1MC1iNjNjLTU4YjU5YzI4NWNjNCIsImlhdCI6MTY4MTkwNTMxMywiZXhwIjoxNjg0NDk3MzEzfQ.HqIloqH6XhZxb1QBtHMNZEL1FYBrHuTxnqRO8towmrk"

    suspend fun getVideo(): List<VideoDevice> {
        val response = retrofitInitialization(token).videoDeviceService.videoDeviceService()
        val devices = mutableListOf<VideoDevice>()
        if (response.isSuccessful) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(ResponseData::class.java)
            val responseData = response.body()?.string()?.let { adapter.fromJson(it) }
            responseData?.data?.let { dataArray ->
                for (deviceJson in dataArray) {
                    val device = VideoDevice(
                        id = deviceJson.id,
                        name = deviceJson.name,
                        type = "Video",
                        serial = deviceJson.serial,
                        username = deviceJson.username,
                        password = deviceJson.password,
                        favorite = "false",
                    )
                    devices.add(device)
                }
            }
        }
        return devices
    }

    data class ResponseData(val count: Int, val data: List<DeviceJson>)
    data class DeviceJson(
        val id: String,
        val name: String,
        val serial: String,
        val username: String,
        val password: String
    )
}
