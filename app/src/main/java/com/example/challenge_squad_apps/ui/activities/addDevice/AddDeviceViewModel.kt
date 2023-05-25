package com.example.challenge_squad_apps.ui.activities.addDevice

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException

class AddDeviceViewModel : ViewModel() {
    private val webClient by lazy { WebClient() }
    private var _addDeviceLiveData: MutableLiveData<Int> = MutableLiveData()
    val addDeviceLiveData = _addDeviceLiveData

    private var _addDeviceErrorLiveData: MutableLiveData<Int> = MutableLiveData()
    val addDeviceErrorLiveData = _addDeviceErrorLiveData


    @SuppressLint("CheckResult")
    fun addDevice(deviceType: String, name: String, serialNumber: String?, user: String?, macAddress: String?, password: String) {
        if (deviceType == DeviceType.ALARME.type) {
            val createdDevice = AlarmDevice(
                id = "0",
                name = name,
                macAddress = macAddress,
                password = password,
            )

            webClient.addDevice(createdDevice)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({responseCode ->
                    _addDeviceLiveData.value = responseCode
                }, { error ->
                    when (error) {
                        is HttpException -> _addDeviceErrorLiveData.value = error.code()
                    }
                })

        } else {
            val createdDevice = VideoDevice(
                id = "0",
                name = name,
                serial = serialNumber,
                username = user,
                password = password,
            )

            webClient.addDevice(createdDevice)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({responseCode ->
                    _addDeviceLiveData.value = responseCode
                }, { error ->
                    when (error) {
                        is HttpException -> _addDeviceErrorLiveData.value = error.code()
                    }
                })
        }
    }
}