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
import com.example.challenge_squad_apps.database.AppDataBase
import com.example.challenge_squad_apps.database.dao.FavoritesDao
import com.example.challenge_squad_apps.databinding.MainActivityBinding
import com.example.challenge_squad_apps.ui.recyclerview.RecyclerViewAdapter
import com.example.challenge_squad_apps.ui.recyclerview.RecyclerViewListener
import com.example.challenge_squad_apps.ui.utils.dialogs.DeleteDeviceDialog
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.DeviceType
import com.example.challenge_squad_apps.webclient.dto.models.Favorites
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), RecyclerViewListener, DeleteDeviceDialog.DeleteDeviceDialogListener {

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
        setupView()
    }

    private fun setupView() {
        lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch {
            updateList()
            setupRecyclerView()
            configDeviceListView()
        }
    }

    private suspend fun updateList() {
        if (deviceList.isNotEmpty()) deviceList.clear()
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
            val intent = Intent(this, AddDeviceActivity::class.java)
            startActivity(intent)

        }
    }

    private fun configureSearchBar() {
        binding.fillSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val deviceFiltered: MutableList<Device>

                deviceFiltered = if (s.isEmpty()) {
                    deviceList
                } else (
                        deviceList.filter { device -> device.name.contains(s, ignoreCase = true) }.toMutableList()
                        )
                updateRecyclerView(deviceFiltered)
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
    private fun updateRecyclerView(deviceList: MutableList<Device>) {
        recyclerViewAdapter.submitList(deviceList)
        recyclerViewAdapter.notifyDataSetChanged()
    }


    private fun configDeviceListView() {

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem
                .itemId) {
                R.id.homeMenuItem -> {
                    updateRecyclerView(deviceList)
                    true
                }

                R.id.videoDevicesMenuItem -> {
                    val videoDevices: MutableList<Device> = deviceList.filter { it.type == DeviceType.VIDEO.type }.toMutableList()
                    updateRecyclerView(videoDevices)
                    true
                }

                R.id.alarmDevicesMenuItem -> {
                    val alarmDevices: MutableList<Device> = deviceList.filter { it.type == DeviceType.ALARM.type }.toMutableList()
                    updateRecyclerView(alarmDevices)
                    true
                }

                R.id.favoriteDevicesMenuItem -> {
                    updateRecyclerView(getFavorites())
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

    private fun onMenuItemPressed(popup: PopupMenu, device: Device) {
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editMenuItem -> {
                    val intent = Intent(this, EditDeviceActivity::class.java)
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
                    val intent = Intent(this, InfoDeviceActivity::class.java)
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
                    DeleteDeviceDialog.newInstance(device)
//                    updateList()
//                    updateRecyclerView(deviceList)
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

        with(popup) {

            menuInflater.inflate(R.menu.dropdown_menu, menu)
            MenuCompat.setGroupDividerEnabled(popup.menu, true)
            setForceShowIcon(true)

            if (favoritesDao.haveFavoriteDevice(device.id)) {
                menu.findItem(R.id.favoriteMenuItem).isVisible = false
                menu.findItem(R.id.unfavoriteMenuItem).isVisible = true
            } else {
                menu.findItem(R.id.favoriteMenuItem).isVisible = true
                menu.findItem(R.id.unfavoriteMenuItem).isVisible = false
            }

            show()
            onMenuItemPressed(this, device)
        }

    }

    override suspend fun confirmButtonClicked(device: Device): Boolean {
        return if (device is AlarmDevice) {
            webClient.deleteAlarm(device.id)
        } else {
            webClient.deleteVideo(device.id)
        }
    }
}





