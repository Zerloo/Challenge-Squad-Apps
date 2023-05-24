package com.example.challenge_squad_apps.ui.activities.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challenge_squad_apps.database.AppDataBase
import com.example.challenge_squad_apps.database.Favorites
import com.example.challenge_squad_apps.database.dao.FavoritesDao
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val webClient by lazy { WebClient() }
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var dataBase: AppDataBase

    private val _deviceListLiveData = MutableLiveData<MutableList<Device>>()
    val deviceListLiveData = _deviceListLiveData

    private val _filteredDeviceListLiveData = MutableLiveData<MutableList<Device>>()
    val filteredDeviceListLiveData = _filteredDeviceListLiveData

    private val _deleteDeviceLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val deleteDeviceLiveData = _deleteDeviceLiveData


    fun roomInstance(applicationContext: Context) {
        dataBase = AppDataBase.instance(applicationContext)
        favoritesDao = dataBase.favoritesDeviceDao()
    }

    @SuppressLint("CheckResult")
    fun updateDeviceList() {
        val deviceList: MutableList<Device> = mutableListOf()
        webClient.getAlarm()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { alarmList ->
                for (alarmDevice in alarmList) {
                    val device = AlarmDevice(
                        id = alarmDevice.id,
                        name = alarmDevice.name,
                        macAddress = alarmDevice.macAddress,
                        password = alarmDevice.password
                    )
                    deviceList.add(device)
                }
                _deviceListLiveData.value = deviceList
            }

        webClient.getVideo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { videoList ->
                for (videoDevice in videoList) {
                    val device = VideoDevice(
                        id = videoDevice.id,
                        name = videoDevice.name,
                        serial = videoDevice.serial,
                        username = videoDevice.username,
                        password = videoDevice.password
                    )
                    deviceList.add(device)
                }
                _deviceListLiveData.value = deviceList
            }
    }
    fun getDeviceList (){
        _filteredDeviceListLiveData.value = _deviceListLiveData.value
    }

    fun getFavorites() {
        val favorites = favoritesDao.getFavoriteDeviceList()
        val devicesFavoriteList = mutableListOf<Device>()

        favorites.forEach { favorite ->
            _deviceListLiveData.value?.find { it.id == favorite.id }?.let { devicesFavoriteList.add(it) }
        }

        _filteredDeviceListLiveData.value = devicesFavoriteList
    }

    fun getVideoDevices() {
        _filteredDeviceListLiveData.value = _deviceListLiveData.value?.filterIsInstance<VideoDevice>()?.toMutableList()
    }

    fun getAlarmDevices() {
        _filteredDeviceListLiveData.value =_deviceListLiveData.value?.filterIsInstance<AlarmDevice>()?.toMutableList()
    }

    fun deleteFavorite(deviceId: String): Boolean {
        return favoritesDao.deleteFavoriteDevice(deviceId) != 0
    }

    fun saveFavorite(deviceId: String) {
        return favoritesDao.saveFavoriteDevice(Favorites(deviceId))
    }

    fun searchDevice(s: CharSequence) {
        _filteredDeviceListLiveData.value = _deviceListLiveData.value?.filter { device -> device.name.contains(s, ignoreCase = true) }?.toMutableList()
    }

    @SuppressLint("CheckResult")
    fun confirmButtonClicked(device: Device) {
        if (device is AlarmDevice) {
            webClient.deleteAlarm(device.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    _deleteDeviceLiveData.value = response
                }
        } else {
            webClient.deleteVideo(device.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    _deleteDeviceLiveData.value = response
                }
        }
    }

    fun checkFavorite(device: Device): Boolean {
        return favoritesDao.isFavoriteDevice(device.id)
    }
}