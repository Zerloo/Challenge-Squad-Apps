package com.example.challenge_squad_apps.ui.activities.main

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.MainActivityBinding
import com.example.challenge_squad_apps.ui.activities.addDevice.AddDeviceActivity
import com.example.challenge_squad_apps.ui.activities.editDevice.EditDeviceActivity
import com.example.challenge_squad_apps.ui.activities.infoDevice.InfoDeviceActivity
import com.example.challenge_squad_apps.ui.utils.dialogs.DeleteDeviceDialog
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MainDeviceListListener, DeleteDeviceDialog.DeleteDeviceDialogListener {


    private lateinit var binding: MainActivityBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var lifecycleScope: LifecycleCoroutineScope
    private lateinit var mainDeviceListAdapter: MainDeviceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.roomInstance(applicationContext)

        setupView()
    }

    override fun onResume() {
        super.onResume()
        binding.fab.isClickable = true
    }

    private fun configureSearchBar() {
        binding.fillSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val deviceFiltered: MutableList<Device>

                deviceFiltered = if (s.isEmpty()) {
                    mainViewModel.deviceList
                } else {
                    mainViewModel.searchDevice(s)
                }
                updateRecyclerView(deviceFiltered)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun configureFab() {
        with(binding.fab) {
            binding.fab.setOnClickListener {
                binding.fab.isClickable = false
                val intent = Intent(this@MainActivity, AddDeviceActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupView() {
        configureSearchBar()
        configureFab()
        lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch {
            setupRecyclerView()
            configDeviceListView()
        }
    }


    private suspend fun setupRecyclerView() {
        mainDeviceListAdapter = MainDeviceListAdapter(this)
        mainDeviceListAdapter.submitList(mainViewModel.updateDeviceList())
        binding.deviceListRecyclerView.apply { adapter = mainDeviceListAdapter }
    }

    private fun configDeviceListView() {

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeMenuItem -> {
                    updateRecyclerView(mainViewModel.deviceList)
                    true
                }

                R.id.videoDevicesMenuItem -> {
                    updateRecyclerView(mainViewModel.getVideoDevices())
                    true
                }

                R.id.alarmDevicesMenuItem -> {
                    updateRecyclerView(mainViewModel.getAlarmDevices())
                    true
                }

                R.id.favoriteDevicesMenuItem -> {
                    updateRecyclerView(mainViewModel.getFavorites())
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

                    intent.putExtra("id", device.id)

                    if (device is VideoDevice) {
                        intent.apply {
                            putExtra("Rastreability", device.serial)
                            putExtra("Type", DeviceType.VIDEO.type)
                        }
                    } else if (device is AlarmDevice) {
                        intent.apply {
                            putExtra("Rastreability", device.macAddress)
                            putExtra("Type", DeviceType.ALARM.type)
                        }
                    }

                    this.startActivity(intent)
                    true
                }

                R.id.infoMenuItem -> {
                    val intent = Intent(this, InfoDeviceActivity::class.java)
                    if (device is AlarmDevice) {
                        intent.apply {
                            putExtra("Mac Address", device.macAddress)
                            putExtra("Type", DeviceType.ALARM.type)
                        }

                    } else if (device is VideoDevice) {
                        intent.apply {
                            putExtra("Serial", device.serial)
                            putExtra("Username", device.username)
                            putExtra("Type", DeviceType.VIDEO.type)
                        }
                    }

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

    override suspend fun confirmButtonClicked(device: Device) {
        val successOnDelete = mainViewModel.confirmButtonClicked(device)

        if (successOnDelete) {
            Toast.makeText(this@MainActivity, "Dispositivo removido!", Toast.LENGTH_SHORT).show()
            updateRecyclerView(mainViewModel.updateDeviceList())
        } else {
            Toast.makeText(this, "Falha ao remover dispositivo!", Toast.LENGTH_SHORT).show()
        }
    }
}





