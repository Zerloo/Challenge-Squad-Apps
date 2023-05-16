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

    suspend fun getAlarm(): MutableList<AlarmDevice> {
        val response = RetrofitInitialization(Constants.token).alarmDeviceService.alarmDeviceService()
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
        val response = RetrofitInitialization(Constants.token).videoDeviceService.videoDeviceService()
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
        val response = RetrofitInitialization(Constants.token).alarmDeviceService.deleteAlarmDeviceService(id)
        return responseStatus(response)
    }

    suspend fun deleteVideo(id: String): Boolean {
        val response = RetrofitInitialization(Constants.token).videoDeviceService.deleteVideoDeviceService(id)
        return responseStatus(response)
    }

    suspend fun editVideo(id: String, newDeviceName: String?, newDeviceUsername: String?, newDevicePassword: String?): Boolean {

        val editedDevice = EditDevice(
            name = newDeviceName,
            username = newDeviceUsername,
            password = newDevicePassword,
        )

        val response = RetrofitInitialization(Constants.token).videoDeviceService.patchVideoDevice(id = id, device = editedDevice)
        return responseStatus(response)
    }

    suspend fun editAlarm(id: String, newDeviceName: String?, newDevicePassword: String?): Boolean {
        val editedDevice = EditDevice(
            name = newDeviceName,
            password = newDevicePassword,
            username = null,
        )

        val response = RetrofitInitialization(Constants.token).alarmDeviceService.patchAlarmDevice(id = id, device = editedDevice)
        return responseStatus(response)
    }

    suspend fun addDevice(device: Device): Boolean {
        lateinit var response: Response<ResponseBody>

        if (device is VideoDevice) {
            response = RetrofitInitialization(Constants.token).videoDeviceService.postVideoDevice(device)
        } else if (device is AlarmDevice) {
            response = RetrofitInitialization(Constants.token).alarmDeviceService.postAlarmDevice(device)
        }

        return responseStatus(response)
    }

    private fun responseStatus(response: Response<ResponseBody>): Boolean {
        return response.isSuccessful
    }
}

