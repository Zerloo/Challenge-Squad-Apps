package com.example.challenge_squad_apps.webclient

import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.EditDevice
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import com.example.challenge_squad_apps.webclient.dto.models.pokos.AlarmResponseData
import com.example.challenge_squad_apps.webclient.dto.models.pokos.VideoResponseData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Response

class WebClient {
    private val token: String =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjZlOWFkM2E2LWQyYTUtNDg1MC1iNjNjLTU4YjU5YzI4NWNjNCIsImlhdCI6MTY4MTkwNTMxMywiZXhwIjoxNjg0NDk3MzEzfQ.HqIloqH6XhZxb1QBtHMNZEL1FYBrHuTxnqRO8towmrk"

    suspend fun getAlarm(): MutableList<AlarmDevice> {
        val response = RetrofitInitialization(token).alarmDeviceService.alarmDeviceService()
        val devices: MutableList<AlarmDevice> = mutableListOf()
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

                        )
                    devices.add(device)
                }
            }
        }
        return devices
    }

    suspend fun getVideo(): MutableList<VideoDevice> {
        val response = RetrofitInitialization(token).videoDeviceService.videoDeviceService()
        val devices: MutableList<VideoDevice> = mutableListOf()
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
                    )
                    devices.add(device)
                }
            }
        }
        return devices
    }

    suspend fun deleteAlarm(id: String): Boolean {
        val response = RetrofitInitialization(token).alarmDeviceService.deleteAlarmDeviceService(id)
        return responseStatus(response)
    }

    suspend fun deleteVideo(id: String): Boolean {
        val response = RetrofitInitialization(token).videoDeviceService.deleteVideoDeviceService(id)
        return responseStatus(response)
    }

    suspend fun editVideo(id: String, newDeviceName: String?, newDeviceUsername: String?, newDevicePassword: String?): Boolean {

        val editedDevice = EditDevice(
            name = newDeviceName,
            username = newDeviceUsername,
            password = newDevicePassword,
        )

        val response = RetrofitInitialization(token).videoDeviceService.patchVideoDevice(id = id, device = editedDevice)
        return responseStatus(response)
    }

    suspend fun editAlarm(id: String, newDeviceName: String?, newDevicePassword: String?): Boolean {
        val editedDevice = EditDevice(
            name = newDeviceName,
            password = newDevicePassword,
            username = null,
        )

        val response = RetrofitInitialization(token).alarmDeviceService.patchAlarmDevice(id = id, device = editedDevice)
        return responseStatus(response)
    }

    suspend fun addDevice(device: Device): Boolean {
        lateinit var response: Response<ResponseBody>

        if (device is VideoDevice) {
            response = RetrofitInitialization(token).videoDeviceService.postVideoDevice(device)
        } else if (device is AlarmDevice) {
            response = RetrofitInitialization(token).alarmDeviceService.postAlarmDevice(device)
        }

        return responseStatus(response)
    }

    private fun responseStatus(response: Response<ResponseBody>): Boolean {
        return response.isSuccessful
    }
}

