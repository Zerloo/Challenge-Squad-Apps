package com.example.challenge_squad_apps.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.ui.DeviceListAdapter
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.models.Device
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var deviceList: List<Device>
    private lateinit var lifecycleScope: LifecycleCoroutineScope
    private val webClient by lazy { WebClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        updateView()
    }

    override fun onResume() {
        super.onResume()
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateView() {
        lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch {
            deviceList = webClient.getVideo()
            deviceList = webClient.getAlarm()
            configRecyclerView()
        }
    }

    private fun configRecyclerView(): RecyclerView {
        val deviceListRecyclerView: RecyclerView = findViewById(R.id.deviceListRecyclerView)
        deviceListRecyclerView.adapter = DeviceListAdapter(deviceList)
        configDeviceListView(deviceListRecyclerView)
        return deviceListRecyclerView
    }

    private fun configDeviceListView(deviceListRecyclerView: RecyclerView) {
        val bottomAppBarView: BottomAppBar = findViewById(R.id.bottomAppBar)
        bottomAppBarView.setOnMenuItemClickListener { menuItem ->
            when (menuItem
                .itemId) {
                R.id.homeMenuItem -> {
                    deviceListRecyclerView.adapter = DeviceListAdapter(deviceList)
                    true
                }

                R.id.videoDevicesMenuItem -> {
                    val videoDevices = deviceList.filter { it.type == "Video" }
                    deviceListRecyclerView.adapter = DeviceListAdapter(videoDevices)
                    true
                }

                R.id.alarmDevicesMenuItem -> {
                    val alarmDevices = deviceList.filter { it.type == "Alarm" }
                    deviceListRecyclerView.adapter = DeviceListAdapter(alarmDevices)
                    true
                }

                R.id.favoriteDevicesMenuItem -> {
                    val favoriteDevices = deviceList.filter { it.favorite == "true" }
                    deviceListRecyclerView.adapter = DeviceListAdapter(favoriteDevices)
                    true
                }

                else -> false
            }
        }
    }
}


