package com.example.challenge_squad_apps.webclient

import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.EditDevice
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import com.example.challenge_squad_apps.webclient.exceptions.ApiException
import com.example.challenge_squad_apps.webclient.exceptions.HttpResponse
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

class WebClient {

    private val gson = Gson()
    fun getAlarm(): Single<List<AlarmDevice>> {
        return RetrofitInitialization(Constants.TOKEN).alarmDeviceService.alarmDeviceService()
            .subscribeOn(Schedulers.io())
            .map { response -> alarmDeviceListJsonAdapter(response) }
    }

    fun getVideo(): Single<List<VideoDevice>> {
        return RetrofitInitialization(Constants.TOKEN).videoDeviceService.videoDeviceService()
            .subscribeOn(Schedulers.io())
            .map { response -> videoDeviceListJsonAdapter(response) }
    }

    private fun videoDeviceListJsonAdapter(response: Response<ResponseBody>): List<VideoDevice> {
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

    private fun videoDeviceJsonAdapter(response: Response<ResponseBody>): VideoDevice {
        val device = response.body()?.string()?.let { jsonResponse ->
            val jsonDeviceObject = JSONObject(jsonResponse)
            val videoDevice = gson.fromJson(jsonDeviceObject.toString(), VideoDevice::class.java)
            videoDevice
        }

        val testeDevice = VideoDevice("4", "a", "a", "a", "a")

        return device ?: testeDevice
    }

    private fun alarmDeviceJsonAdapter(response: Response<ResponseBody>): AlarmDevice {
        val device = response.body()!!.string().let { jsonResponse ->
            val jsonDeviceObject = JSONObject(jsonResponse)
            val alarmDevice = gson.fromJson(jsonDeviceObject.toString(), AlarmDevice::class.java)
            alarmDevice
        }
        return device
    }

    private fun alarmDeviceListJsonAdapter(response: Response<ResponseBody>): List<AlarmDevice> {
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


    fun deleteAlarm(id: String): Completable {
        return RetrofitInitialization(Constants.TOKEN).alarmDeviceService.deleteAlarmDeviceService(id)
            .doOnError {
                (it as? HttpException)?.let { exception ->
                    val error = HttpResponse.fromCode(exception.code())
                    throw ApiException(error!!)
                }
            }
    }

    fun deleteVideo(id: String): Completable {
        return RetrofitInitialization(Constants.TOKEN).videoDeviceService.deleteVideoDeviceService(id)
            .doOnError {
                (it as? HttpException)?.let { exception ->
                    val error = HttpResponse.fromCode(exception.code())
                    throw ApiException(error!!)
                }
            }
    }

    fun editVideo(id: String, newDeviceName: String?, newDeviceUsername: String?, newDevicePassword: String?): Single<Boolean> {

        val editedDevice = EditDevice(
            name = newDeviceName,
            username = newDeviceUsername,
            password = newDevicePassword,
        )

        val response = RetrofitInitialization(Constants.TOKEN).videoDeviceService.patchVideoDevice(id = id, device = editedDevice)
        return response
            .map { response ->
                response.isSuccessful
            }
    }

    fun editAlarm(id: String, newDeviceName: String?, newDevicePassword: String?): Single<Boolean> {
        val editedDevice = EditDevice(
            name = newDeviceName,
            password = newDevicePassword,
            username = null,
        )

        val response = RetrofitInitialization(Constants.TOKEN).alarmDeviceService.patchAlarmDevice(id = id, device = editedDevice)
        return response
            .map { response -> response.isSuccessful }
    }

    fun addVideoDevice(device: Device): Single<VideoDevice> {
        return RetrofitInitialization(Constants.TOKEN).videoDeviceService.postVideoDevice(device as VideoDevice)
            .map { response ->
                if (response.code() != HttpResponse.HTTP_201_CREATED.code) {
                    throw ApiException(HttpResponse.fromCode(response.code())!!)
                } else {
                    videoDeviceJsonAdapter(response)
                }
            }
            .doOnError {
                (it as? HttpException)?.let { exception ->
                    val error = HttpResponse.fromCode(exception.code())
                    throw ApiException(error!!)
                }
            }
    }

    fun addAlarmDevice(device: Device): Single<AlarmDevice> {
        return RetrofitInitialization(Constants.TOKEN).alarmDeviceService.postAlarmDevice(device as AlarmDevice)
            .map { response ->
                if (response.code() != HttpResponse.HTTP_201_CREATED.code) {
                    throw ApiException(HttpResponse.fromCode(response.code())!!)
                } else {
                    alarmDeviceJsonAdapter(response)
                }
            }
            .doOnError {
                (it as? HttpException)?.let { exception ->
                    val error = HttpResponse.fromCode(exception.code())
                    throw ApiException(error!!)
                }
            }
    }
}

