package com.example.challenge_squad_apps.webclient

import com.example.challenge_squad_apps.webclient.models.AddDevice
import com.example.challenge_squad_apps.webclient.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.models.AlarmDeviceJson
import com.example.challenge_squad_apps.webclient.models.EditDevice
import com.example.challenge_squad_apps.webclient.models.VideoDevice
import com.example.challenge_squad_apps.webclient.models.VideoDeviceJson
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
//                        favorite = false,

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
//                        favorite = false,
                    )
                    devices.add(device)
                }
            }
        }
        return devices
    }

    suspend fun getVideoById(id: String): VideoDevice {
        val response = RetrofitInitialization(token).videoDeviceService.byIdVideoDeviceService(id)
        lateinit var device: VideoDevice

        if (response.isSuccessful) {

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(VideoDeviceJson::class.java)

            val responseData = response.body()?.string().let {
                adapter.fromJson(it)
            }

            responseData?.let {
                device = VideoDevice(
                    id = it.id,
                    name = it.name,
                    type = "Video",
                    serial = it.serial,
                    username = it.username,
                    password = it.password,
                )
            }
        }
        return device
    }

    suspend fun deleteAlarm(id: String): Boolean {
        val response = RetrofitInitialization(token).alarmDeviceService.deleteAlarmDeviceService(id)
        return responseStatus(response)
    }

    suspend fun deleteVideo(id: String): Boolean {
        val response = RetrofitInitialization(token).videoDeviceService.deleteVideoDeviceService(id)
        return responseStatus(response)
    }

    suspend fun editVideo(id: String, newDeviceName: String, newDeviceUsername: String, newDevicePassword: String): Boolean {

        val editedDevice = EditDevice(
            name = newDeviceName,
            username = newDeviceUsername,
            password = newDevicePassword,
        )

        val response = RetrofitInitialization(token).videoDeviceService.patchVideoDevice(id = id, device = editedDevice)
        return responseStatus(response)
    }

    suspend fun editAlarm(id: String, newDeviceName: String, newDevicePassword: String): Boolean {
        val editedDevice = EditDevice(
            name = newDeviceName,
            password = newDevicePassword,
            username = null,
        )

        val response = RetrofitInitialization(token).alarmDeviceService.patchAlarmDevice(id = id, device = editedDevice)
        return responseStatus(response)
    }

    suspend fun addDevice(newName: String, newSerialNumber: String?, newUser: String?, newMacAddress: String?, newPassword: String, deviceType: String): Boolean {
        lateinit var response: Response<ResponseBody>

        val createdDevice = AddDevice(
            name = newName,
            serial = newSerialNumber,
            username = newUser,
            macAddress = newMacAddress,
            password = newPassword
        )

        if (deviceType == "Vídeo") {
            response = RetrofitInitialization(token).videoDeviceService.postVideoDevice(device = createdDevice)
        } else if (deviceType == "Alarm") {
            response = RetrofitInitialization(token).alarmDeviceService.postAlarmDevice(device = createdDevice)
        }

        return responseStatus(response)
    }

    data class VideoResponseData(val count: Int, val data: List<VideoDeviceJson>)

    data class AlarmResponseData(val count: Int, val data: List<AlarmDeviceJson>)

    private fun responseStatus(response: Response<ResponseBody>): Boolean {
        return response.isSuccessful
    }
}

