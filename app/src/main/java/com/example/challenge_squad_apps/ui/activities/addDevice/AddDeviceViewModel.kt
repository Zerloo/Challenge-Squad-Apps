package com.example.challenge_squad_apps.ui.activities.addDevice

import android.annotation.SuppressLint
import android.provider.MediaStore.Video
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import com.example.challenge_squad_apps.webclient.exceptions.ApiException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.lang.NullPointerException

class AddDeviceViewModel : ViewModel() {
    private val webClient by lazy { WebClient() }
    private lateinit var disposable: Disposable

    private var _addDeviceLiveData: MutableLiveData<Device> = MutableLiveData()
    val addDeviceLiveData = _addDeviceLiveData

    private var _addDeviceErrorLiveData: MutableLiveData<Int> = MutableLiveData()
    val addDeviceErrorLiveData = _addDeviceErrorLiveData


    @SuppressLint("CheckResult")
    fun addAlarmDevice(name: String, macAddress: String, password: String) {
        val createdDevice = AlarmDevice(
            id = "0",
            name = name,
            macAddress = macAddress,
            password = password,
        )

        disposable = webClient.addAlarmDevice(createdDevice)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ device ->
                disposable?.dispose()
                _addDeviceLiveData.value = device
            }, { error ->
                when (error) {
                    is ApiException -> _addDeviceErrorLiveData.value = error.error.code
                }
            })
    }

    fun addVideoDevice(name: String, serialNumber: String, user: String, password: String) {
        val createdDevice = VideoDevice(
            id = "0",
            name = name,
            serial = serialNumber,
            username = user,
            password = password,
        )

        disposable = webClient.addVideoDevice(createdDevice)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ device ->
                disposable?.dispose()
                _addDeviceLiveData.value = device
            }, { error ->
                when (error) {
                    is ApiException -> _addDeviceErrorLiveData.value = error.error.code
                }
            })
    }
}