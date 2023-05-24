package com.example.challenge_squad_apps.ui.activities.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.Video
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuCompat
import androidx.lifecycle.ViewModelProvider
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.MainActivityBinding
import com.example.challenge_squad_apps.ui.activities.addDevice.AddDeviceActivity
import com.example.challenge_squad_apps.ui.activities.editDevice.EditDeviceActivity
import com.example.challenge_squad_apps.ui.activities.infoDevice.InfoDeviceActivity
import com.example.challenge_squad_apps.ui.utils.dialogs.DeleteDeviceDialog
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice

class MainActivity : AppCompatActivity(), MainDeviceListListener, DeleteDeviceDialog.DeleteDeviceDialogListener {


    private lateinit var binding: MainActivityBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainDeviceListAdapter: MainDeviceListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.roomInstance(applicationContext)

        setupView()
        registerObservers()
    }

    override fun onResume() {
        super.onResume()
        binding.fab.isClickable = true
        mainViewModel.updateDeviceList()
    }


    private fun configureSearchBar() {
        binding.fillSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mainViewModel.searchDevice(s)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun configureFab() {
        with(binding.fab) {
            setOnClickListener {
                isClickable = false
                val intent = Intent(this@MainActivity, AddDeviceActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupView() {
        configureSearchBar()
        configureFab()
        setupRecyclerView()
        configDeviceListView()
    }


    private fun setupRecyclerView() {
        mainDeviceListAdapter = MainDeviceListAdapter(this)
        binding.deviceListRecyclerView.apply { adapter = mainDeviceListAdapter }
        mainViewModel.updateDeviceList()
    }

    private fun registerObservers() {
        mainViewModel.deviceListLiveData.observe(this) { deviceList ->
            deviceList.sortBy { device ->
                when (device) {
                    is VideoDevice -> 0
                    is AlarmDevice -> 1
                    else -> 2
                }
            }
            updateRecyclerView(deviceList)
        }

        mainViewModel.deleteDeviceLiveData.observe(this) { responseStatus ->
            showDeleteDeviceToast(responseStatus)
        }

        mainViewModel.filteredDeviceListLiveData.observe(this) { deviceList ->
            updateRecyclerView(deviceList)
        }
    }


    private fun configDeviceListView() {

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeMenuItem -> {
                    mainViewModel.getDeviceList()
                    true
                }

                R.id.videoDevicesMenuItem -> {
                    mainViewModel.getVideoDevices()
                    true
                }

                R.id.alarmDevicesMenuItem -> {
                    mainViewModel.getAlarmDevices()
                    true
                }

                R.id.favoriteDevicesMenuItem -> {
                    mainViewModel.getFavorites()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(deviceList: MutableList<Device>) {
        mainDeviceListAdapter.apply {
            submitList(deviceList)
            notifyDataSetChanged()
        }
    }


    private fun onMenuItemPressed(popup: PopupMenu, device: Device) {
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editMenuItem -> {
                    val intent = Intent(this, EditDeviceActivity::class.java)

                    sendDeviceInformation(intent, device)

                    this.startActivity(intent)
                    true
                }

                R.id.infoMenuItem -> {
                    val intent = Intent(this, InfoDeviceActivity::class.java)

                    sendDeviceInformation(intent, device)

                    this.startActivity(intent)
                    true
                }

                R.id.unfavoriteMenuItem -> {
                    if (mainViewModel.deleteFavorite(device.id)) Toast.makeText(this, "Favorito removido!", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.favoriteMenuItem -> {
                    mainViewModel.saveFavorite(device.id)
                    Toast.makeText(this, "Favorito adicionado", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.deleteMenuItem -> {
                    val dialog = DeleteDeviceDialog.newInstance(device)
                    dialog.show(supportFragmentManager, DeleteDeviceDialog.TAG)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun sendDeviceInformation(intent: Intent, device: Device) {
        intent.apply {
            putExtra("id", device.id)
            putExtra("name", device.name)
            putExtra("password", device.password)

            if (device is VideoDevice) {
                putExtra("type", DeviceType.VIDEO.type)
                putExtra("username", device.username)
                putExtra("rastreability", device.serial)
            } else if (device is AlarmDevice) {
                putExtra("type", DeviceType.ALARM.type)
                putExtra("rastreability", device.macAddress)
            }
        }
    }

    override fun onDropdownPressed(view: View, device: Device) {
        val popup = PopupMenu(this, view)

        with(popup) {

            menuInflater.inflate(R.menu.dropdown_menu, menu)
            MenuCompat.setGroupDividerEnabled(popup.menu, true)
            setForceShowIcon(true)

            with(menu) {
                if (mainViewModel.checkFavorite(device)) {
                    findItem(R.id.favoriteMenuItem).isVisible = false
                    findItem(R.id.unfavoriteMenuItem).isVisible = true
                } else {
                    findItem(R.id.favoriteMenuItem).isVisible = true
                    findItem(R.id.unfavoriteMenuItem).isVisible = false
                }
            }

            show()
            onMenuItemPressed(this, device)
        }
    }

    override fun confirmButtonClicked(device: Device) {
        mainViewModel.confirmButtonClicked(device)
    }

    private fun showDeleteDeviceToast(responseStatus: Boolean) {
        if (responseStatus) {
            Toast.makeText(this@MainActivity, "Dispositivo removido!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Falha ao remover dispositivo!", Toast.LENGTH_SHORT).show()
        }
    }
}





