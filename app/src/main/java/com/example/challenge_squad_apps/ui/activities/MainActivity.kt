package com.example.challenge_squad_apps.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.dao.FavoritesDao
import com.example.challenge_squad_apps.database.AppDataBase
import com.example.challenge_squad_apps.ui.DeviceListAdapter
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.models.Device
import com.example.challenge_squad_apps.webclient.models.Favorites
import com.example.challenge_squad_apps.webclient.models.VideoDevice
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var deviceList: MutableList<Device>
    private lateinit var lifecycleScope: LifecycleCoroutineScope
    private lateinit var dataBase: AppDataBase
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var deviceListAdapter: DeviceListAdapter
    private val webClient by lazy { WebClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        dataBase = AppDataBase.instancia(this)

        configureFab()
        updateView()
    }

    private fun updateView() {
        lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch {
            val videoDeviceList = webClient.getVideo()
            val alarmDeviceList = webClient.getAlarm()

            deviceList = videoDeviceList.toMutableList()
            deviceList.addAll(alarmDeviceList)
            configRecyclerView()
        }
    }

    private fun configureFab() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configRecyclerView(): RecyclerView {
        val deviceListRecyclerView: RecyclerView = findViewById(R.id.deviceListRecyclerView)
        deviceListAdapter = DeviceListAdapter(deviceList)
        deviceListRecyclerView.adapter = deviceListAdapter
        configDeviceListView(deviceListRecyclerView)
        return deviceListRecyclerView
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun configDeviceListView(deviceListRecyclerView: RecyclerView) {
        val bottomAppBarView: BottomAppBar = findViewById(R.id.bottomAppBar)
        bottomAppBarView.setOnMenuItemClickListener { menuItem ->
            when (menuItem
                .itemId) {
                R.id.homeMenuItem -> {
//                    deviceListRecyclerView.adapter = DeviceListAdapter(deviceList, dataBase)
                    deviceListAdapter.deviceList = deviceList
                    deviceListAdapter.notifyDataSetChanged()
                    true
                }

                R.id.videoDevicesMenuItem -> {
                    val videoDevices: MutableList<Device> = deviceList.filter { it.type == "Video" } as MutableList<Device>
//                    deviceListRecyclerView.adapter = DeviceListAdapter(videoDevices, dataBase)
                    deviceListAdapter.deviceList = videoDevices
                    deviceListAdapter.notifyDataSetChanged()
                    true
                }

                R.id.alarmDevicesMenuItem -> {
                    val alarmDevices: MutableList<Device> = deviceList.filter { it.type == "Alarm" } as MutableList<Device>
//                    deviceListRecyclerView.adapter = DeviceListAdapter(alarmDevices, dataBase)
                    deviceListAdapter.deviceList = alarmDevices
                    deviceListAdapter.notifyDataSetChanged()
                    true
                }

                R.id.favoriteDevicesMenuItem -> {
//                    deviceListRecyclerView.adapter = DeviceListAdapter(getFavorites())
                    deviceListAdapter.deviceList = getFavorites()
                    deviceListAdapter.notifyDataSetChanged()
                    true
                }

                else -> false
            }
        }
    }

    private fun getFavorites(): MutableList<Device> {
        val favorites = favoritesDao.getFavoriteDeviceList()
        val devicesFavoriteList = mutableListOf<Device>()

        deviceList.forEach { device ->
            favorites.forEach { favorite ->
                if (device.id == favorite.id) devicesFavoriteList.add(device)
            }
        }

        return devicesFavoriteList
    }
}


