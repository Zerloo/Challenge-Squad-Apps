package com.example.challenge_squad_apps.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.coroutineScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.dao.FavoritesDao
import com.example.challenge_squad_apps.database.AppDataBase
import com.example.challenge_squad_apps.databinding.MainActivityBinding
import com.example.challenge_squad_apps.ui.RecyclerViewAdapter
import com.example.challenge_squad_apps.ui.RecyclerViewListener
import com.example.challenge_squad_apps.webclient.DeleteDevice
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.models.Device
import com.example.challenge_squad_apps.webclient.models.Favorites
import com.example.challenge_squad_apps.webclient.models.VideoDevice
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), RecyclerViewListener {

    private val webClient by lazy { WebClient() }

    private var deviceList: MutableList<Device> = mutableListOf()
    private lateinit var binding: MainActivityBinding
    private lateinit var dataBase: AppDataBase
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var lifecycleScope: LifecycleCoroutineScope
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBase = AppDataBase.instance(applicationContext)
        favoritesDao = dataBase.favoritesDeviceDao()


        configureSearchBar()
        configureFab()

        lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch { setupView() }
    }

    private suspend fun setupView() {
        lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch {
            updateList()
            setupRecyclerView()
            configDeviceListView()
        }.join()
    }

    private suspend fun updateList() {
        if (deviceList.isNotEmpty()) deviceList.removeAll(deviceList)
        lifecycleScope.launch {
            val videoDeviceList = webClient.getVideo()
            val alarmDeviceList = webClient.getAlarm()
            deviceList.addAll(videoDeviceList)
            deviceList.addAll(alarmDeviceList)
        }.join()
    }

    private fun configureFab() {
        binding.fab.setOnClickListener {
            binding.fab.isEnabled = false
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)

            val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
            backgroundExecutor.schedule({
                binding.fab.isEnabled = true
                backgroundExecutor.shutdownNow()
            }, 3, TimeUnit.SECONDS)
        }
    }

    private fun configureSearchBar() {
        binding.fillSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                var deviceFiltered: MutableList<Device> = mutableListOf()

                    deviceFiltered = if (s.isEmpty()) {
                    deviceList
                } else ({
                    deviceList.filter { device ->
                        when (device) {
                            is AlarmDevice -> device.name.contains(s, ignoreCase = true)
                            is VideoDevice -> device.name.contains(s, ignoreCase = true)
                            else -> false
                        }
                    }
                }as MutableList<Device>)

                recyclerViewAdapter.submitList(deviceFiltered)
                recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }


    private fun setupRecyclerView() {
        recyclerViewAdapter = RecyclerViewAdapter(this)
        recyclerViewAdapter.submitList(deviceList)
        binding.deviceListRecyclerView.apply { adapter = recyclerViewAdapter }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun configDeviceListView() {

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem
                .itemId) {
                R.id.homeMenuItem -> {
                    recyclerViewAdapter.submitList(deviceList)
                    recyclerViewAdapter.notifyDataSetChanged()
                    true
                }

                R.id.videoDevicesMenuItem -> {
                    val videoDevices: MutableList<Device> = deviceList.filter { it.type == "Video" } as MutableList<Device>
                    recyclerViewAdapter.submitList(videoDevices)
                    recyclerViewAdapter.notifyDataSetChanged()
                    true
                }

                R.id.alarmDevicesMenuItem -> {
                    val alarmDevices: MutableList<Device> = deviceList.filter { it.type == "Alarm" } as MutableList<Device>
                    recyclerViewAdapter.submitList(alarmDevices)
                    recyclerViewAdapter.notifyDataSetChanged()
                    true
                }

                R.id.favoriteDevicesMenuItem -> {
                    recyclerViewAdapter.submitList(getFavorites())
                    recyclerViewAdapter.notifyDataSetChanged()
                    true
                }

                else -> {
                    false
                }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun onMenuItemPressed(popup: PopupMenu, device: Device) {
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editMenuItem -> {
                    val intent = Intent(this, EditActivity::class.java)
                    intent.putExtra("Type", device.type)
                    intent.putExtra("id", device.id)

                    if (device is VideoDevice) {
                        intent.putExtra("rastreability", device.serial)
                    } else if (device is AlarmDevice) {
                        intent.putExtra("rastreability", device.macAddress)
                    }

                    this.startActivity(intent)
                    true
                }

                R.id.infoMenuItem -> {
                    val intent = Intent(this, InfoActivity::class.java)
                    intent.putExtra("Type", device.type)

                    if (device is AlarmDevice) {
                        intent.putExtra("Mac Address", device.macAddress)
                    } else if (device is VideoDevice) {
                        intent.putExtra("Serial", device.serial)
                        intent.putExtra("Username", device.username)
                    }

                    this.startActivity(intent)
                    true
                }

                R.id.unfavoriteMenuItem -> {
                    if (favoritesDao.deleteFavoriteDevice(device.id) != 0) Toast.makeText(this, "Favorito removido!", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.favoriteMenuItem -> {
                    favoritesDao.saveFavoriteDevice(Favorites(device.id))
                    Toast.makeText(this, "Favorito adicionado", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.deleteMenuItem -> {
                    val deleteDevice = DeleteDevice()

                    if (deleteDevice.deleteDropdownMenuDialog(this, device)) {
                        lifecycleScope.launch {
                            updateList()
                            recyclerViewAdapter.submitList(deviceList)
                            recyclerViewAdapter.notifyDataSetChanged()
                        }
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onDropdownPressed(view: View, device: Device) {
        val popup = PopupMenu(this, view)

        popup.menuInflater.inflate(R.menu.dropdown_menu, popup.menu)
        MenuCompat.setGroupDividerEnabled(popup.menu, true)
        popup.setForceShowIcon(true)

        if (favoritesDao.haveFavoriteDevice(device.id)) {
            popup.menu.findItem(R.id.favoriteMenuItem).isVisible = false
            popup.menu.findItem(R.id.unfavoriteMenuItem).isVisible = true
        } else {
            popup.menu.findItem(R.id.favoriteMenuItem).isVisible = true
            popup.menu.findItem(R.id.unfavoriteMenuItem).isVisible = false
        }

        popup.show()
        onMenuItemPressed(popup, device)
    }
}



