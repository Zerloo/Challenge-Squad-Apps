package com.example.challenge_squad_apps.ui.webclient

import com.example.challenge_squad_apps.ui.webclient.models.AlarmDevice
import com.example.challenge_squad_apps.ui.webclient.models.Device
import com.example.challenge_squad_apps.ui.webclient.models.VideoDevice
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class WebClient {
    private val token: String =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjZlOWFkM2E2LWQyYTUtNDg1MC1iNjNjLTU4YjU5YzI4NWNjNCIsImlhdCI6MTY4MTkwNTMxMywiZXhwIjoxNjg0NDk3MzEzfQ.HqIloqH6XhZxb1QBtHMNZEL1FYBrHuTxnqRO8towmrk"
    private val devices = mutableListOf<Device>()

    suspend fun getAlarm(): List<Device> {
        val response = RetrofitInitialization(token).alarmDeviceService.alarmDeviceService()
        if (response.isSuccessful) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(AlarmResponseData::class.java)
            val responseData = response.body()?.string()?.let { adapter.fromJson(it) }
            responseData?.data?.let { dataArray ->
                for (deviceJson in dataArray) {
                    val device = AlarmDevice(
                        id = deviceJson.id,
                        name = deviceJson.name,
                        type = "Alarm",
                        macAddress = deviceJson.macAddress,
                        password = deviceJson.password,
                        favorite = "false",

                        )
                    devices.add(device)
                }
            }
        }
        return devices
    }

    data class AlarmResponseData(val count: Int, val data: List<AlarmDeviceJson>)
    data class AlarmDeviceJson(
        val id: String,
        val name: String,
        val macAddress: String,
        val password: String
    )

    suspend fun getVideo(): List<Device> {
        val response = RetrofitInitialization(token).videoDeviceService.videoDeviceService()
        if (response.isSuccessful) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(VideoResponseData::class.java)
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


    data class VideoResponseData(val count: Int, val data: List<VideoDeviceJson>)
    data class VideoDeviceJson(
        val id: String,
        val name: String,
        val serial: String,
        val username: String,
        val password: String
    )


}
