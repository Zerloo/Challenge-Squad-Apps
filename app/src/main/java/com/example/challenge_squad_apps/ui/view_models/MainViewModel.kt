//package com.example.challenge_squad_apps.ui.view_models
//
//import androidx.lifecycle.LifecycleCoroutineScope
//import androidx.lifecycle.ViewModel
//import com.example.challenge_squad_apps.database.AppDataBase
//import com.example.challenge_squad_apps.database.dao.FavoritesDao
//import com.example.challenge_squad_apps.webclient.WebClient
//import com.example.challenge_squad_apps.webclient.models.Device
//import kotlinx.coroutines.launch
//
//class MainViewModel : ViewModel() {
//    private var deviceList: MutableList<Device> = mutableListOf()
//    private val webClient by lazy { WebClient() }
//    private lateinit var lifecycleScope: LifecycleCoroutineScope
//    private lateinit var dataBase: AppDataBase
//    private lateinit var favoritesDao: FavoritesDao
//
//
//    dataBase = AppDataBase.instance(applicationContext)
//    favoritesDao = dataBase.favoritesDeviceDao()
//
//
//
//    private suspend fun updateList() {
//        if (deviceList.isNotEmpty()) deviceList.clear()
//        lifecycleScope.launch {
//            val videoDeviceList = webClient.getVideo()
//            val alarmDeviceList = webClient.getAlarm()
//            deviceList.addAll(videoDeviceList)
//            deviceList.addAll(alarmDeviceList)
//        }.join()
//    }
//
//
//
//
//}