package com.example.challenge_squad_apps.ui.activities.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
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
    var deviceList: MutableList<Device> = mutableListOf()
        private set
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var dataBase: AppDataBase
    private val _deviceListLiveData = MutableLiveData<MutableList<Device>>()
    val deviceListLiveData = _deviceListLiveData


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


    fun getFavorites(): MutableList<Device> {
        val favorites = favoritesDao.getFavoriteDeviceList()
        val devicesFavoriteList = mutableListOf<Device>()

        favorites.forEach { favorite ->
            deviceList.find { it.id == favorite.id }?.let { devicesFavoriteList.add(it) }
        }

        return devicesFavoriteList
    }

    fun getVideoDevices(): MutableList<Device> {
        return deviceList.filterIsInstance<VideoDevice>().toMutableList()
    }

    fun getAlarmDevices(): MutableList<Device> {
        return deviceList.filterIsInstance<AlarmDevice>().toMutableList()
    }

    fun deleteFavorite(deviceId: String): Boolean {
        return favoritesDao.deleteFavoriteDevice(deviceId) != 0
    }

    fun saveFavorite(deviceId: String) {
        return favoritesDao.saveFavoriteDevice(Favorites(deviceId))
    }

    fun searchDevice(s: CharSequence): MutableList<Device> {
        return deviceList.filter { device -> device.name.contains(s, ignoreCase = true) }.toMutableList()
    }

    suspend fun confirmButtonClicked(device: Device): Boolean {
        return if (device is AlarmDevice) {
            webClient.deleteAlarm(device.id)
        } else {
            webClient.deleteVideo(device.id)
        }
    }

    fun checkFavorite(device: Device): Boolean {
        return favoritesDao.isFavoriteDevice(device.id)
    }
}