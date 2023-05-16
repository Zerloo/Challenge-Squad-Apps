package com.example.challenge_squad_apps.ui.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_squad_apps.database.AppDataBase
import com.example.challenge_squad_apps.database.dao.FavoritesDao
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.database.Favorites
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val webClient by lazy { WebClient() }
    private var deviceList: MutableList<Device> = mutableListOf()
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var dataBase: AppDataBase

    fun roomInstance(applicationContext: Context) {
        dataBase = AppDataBase.instance(applicationContext)
        favoritesDao = dataBase.favoritesDeviceDao()
    }


    suspend fun updateDeviceList(): MutableList<Device> {
        viewModelScope.launch {
            if (deviceList.isNotEmpty()) deviceList.clear()
            val videoDeviceList = webClient.getVideo()
            val alarmDeviceList = webClient.getAlarm()
            deviceList.addAll(videoDeviceList)
            deviceList.addAll(alarmDeviceList)
        }.join()
        return returnDeviceList()
    }

    fun returnDeviceList(): MutableList<Device> {
        return deviceList
    }

    fun getFavorites(): MutableList<Device> {
        val favorites = favoritesDao.getFavoriteDeviceList()
        val devicesFavoriteList = mutableListOf<Device>()

        deviceList.forEach { device ->
            favorites.forEach { favorite ->
                if (device.id == favorite.id) devicesFavoriteList.add(device)
            }
        }
        return devicesFavoriteList
    }

    fun getVideoDevices(): MutableList<Device>{
        return deviceList.filterIsInstance<VideoDevice>().toMutableList()
    }

    fun getAlarmDevices(): MutableList<Device>{
        return deviceList.filterIsInstance<AlarmDevice>().toMutableList()
    }

    fun deleteFavorite(deviceId: String): Boolean {
        return favoritesDao.deleteFavoriteDevice(deviceId) != 0
    }

    fun saveFavorite(deviceId: String){
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
        return favoritesDao.haveFavoriteDevice(device.id)
    }
}