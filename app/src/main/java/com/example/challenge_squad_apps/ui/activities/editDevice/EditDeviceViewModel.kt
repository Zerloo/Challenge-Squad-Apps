package com.example.challenge_squad_apps.ui.activities.editDevice

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.WebClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class EditDeviceViewModel : ViewModel() {

    private val webClient by lazy { WebClient() }
    private var _editDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val editDeviceLiveData = _editDeviceLiveData

    private var _editDeviceErrorLiveData : MutableLiveData<String?> = MutableLiveData()
    val editDeviceErrorLiveData = _editDeviceErrorLiveData

    @SuppressLint("CheckResult")
    fun saveDeviceChanges(deviceId: String?, deviceType: String?, newDeviceName: String?, newDeviceUsername: String?, newDevicePassword: String?) {

        if (deviceType == DeviceType.ALARM.type) {
            webClient.editAlarm(deviceId.toString(), newDeviceName, newDevicePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ response ->
                    _editDeviceLiveData.value = response
                },
                    {error ->
                      _editDeviceErrorLiveData.value = error.message
                    })
        } else {
            webClient.editVideo(deviceId.toString(), newDeviceName, newDeviceUsername, newDevicePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ response ->
                    _editDeviceLiveData.value = response
                },
                    {error ->
                        _editDeviceErrorLiveData.value = error.message
                    })
        }
    }
}
