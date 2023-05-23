package com.example.challenge_squad_apps.webclient

import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.EditDevice
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class WebClient {

    private val gson = Gson()
    fun getAlarm(): Single<List<AlarmDevice>> {
        return RetrofitInitialization(Constants.TOKEN).alarmDeviceService.alarmDeviceService()
            .subscribeOn(Schedulers.io())
            .map { response -> alarmDeviceJsonAdapter(response) }
    }

    fun getVideo(): Single<List<VideoDevice>> {
        return RetrofitInitialization(Constants.TOKEN).videoDeviceService.videoDeviceService()
            .subscribeOn(Schedulers.io())
            .map { response -> videoDeviceJsonAdapter(response) }
    }

    private fun videoDeviceJsonAdapter(response: Response<ResponseBody>): List<VideoDevice> {
        val responseData = response.body()?.string()?.let { jsonResponse ->
            val jsonDeviceObject = JSONObject(jsonResponse)
            val dataArray = jsonDeviceObject.getJSONArray("data")

            val videoDeviceList: MutableList<VideoDevice> = mutableListOf()
            for (i in 0 until dataArray.length()) {
                val videoDeviceObject = dataArray.getJSONObject(i)
                val videoDevice = gson.fromJson(videoDeviceObject.toString(), VideoDevice::class.java)
                videoDeviceList.add(videoDevice)
            }

            videoDeviceList
        }

        return responseData ?: emptyList()
    }

    private fun alarmDeviceJsonAdapter(response: Response<ResponseBody>): List<AlarmDevice> {
        val responseData = response.body()?.string()?.let { jsonString ->
            val jsonObject = JSONObject(jsonString)
            val dataArray = jsonObject.getJSONArray("data")

            val alarmDeviceList: MutableList<AlarmDevice> = mutableListOf()
            for (i in 0 until dataArray.length()) {
                val alarmDeviceObject = dataArray.getJSONObject(i)
                val alarmDevice = gson.fromJson(alarmDeviceObject.toString(), AlarmDevice::class.java)
                alarmDeviceList.add(alarmDevice)
            }

            alarmDeviceList
        }

        return responseData ?: emptyList()
    }


    suspend fun deleteAlarm(id: String): Boolean {
        val response = RetrofitInitialization(Constants.TOKEN).alarmDeviceService.deleteAlarmDeviceService(id)
        return responseStatus(response)
    }

    suspend fun deleteVideo(id: String): Boolean {
        val response = RetrofitInitialization(Constants.TOKEN).videoDeviceService.deleteVideoDeviceService(id)
        return responseStatus(response)
    }

    suspend fun editVideo(id: String, newDeviceName: String?, newDeviceUsername: String?, newDevicePassword: String?): Boolean {

        val editedDevice = EditDevice(
            name = newDeviceName,
            username = newDeviceUsername,
            password = newDevicePassword,
        )

        val response = RetrofitInitialization(Constants.TOKEN).videoDeviceService.patchVideoDevice(id = id, device = gson.toJson(editedDevice))
        return responseStatus(response)
    }

    suspend fun editAlarm(id: String, newDeviceName: String?, newDevicePassword: String?): Boolean {
        val editedDevice = EditDevice(
            name = newDeviceName,
            password = newDevicePassword,
            username = null,
        )

        val response = RetrofitInitialization(Constants.TOKEN).alarmDeviceService.patchAlarmDevice(id = id, device = editedDevice)
        return responseStatus(response)
    }

    fun addDevice(device: Device): Single<Boolean> {
        lateinit var response: Single<Response<ResponseBody>>

        if (device is VideoDevice) {
            response = RetrofitInitialization(Constants.TOKEN).videoDeviceService.postVideoDevice(device)
        } else if (device is AlarmDevice) {
            response = RetrofitInitialization(Constants.TOKEN).alarmDeviceService.postAlarmDevice(device)
        }


        return response
            .map { response -> response.isSuccessful }
    }

    private fun responseStatus(response: Response<ResponseBody>): Boolean {
        return response.isSuccessful
    }
}

