package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.AddDeviceBinding
import com.example.challenge_squad_apps.ui.utils.dialogs.AddDeviceDialog
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.DeviceType
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddDeviceActivity : AppCompatActivity() {

    private lateinit var binding: AddDeviceBinding
    private var deviceType = ""
    private val webClient by lazy {
        WebClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        readDeviceTypeInput()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {

            addDeviceTopAppBar.setNavigationOnClickListener {
                finish()
            }
            addDeviceTopAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save_menu -> {

                        val newName = inputAddDeviceName.text.toString()
                        val newSerialNumber: String? = inputAddDeviceSerialNumber.text.toString()
                        val newUser: String? = inputAddDeviceUser.text.toString()
                        val newMacAddress: String? = inputAddDeviceMacAddress.text.toString()
                        val newPassword = inputAddDevicePassword.text.toString()

                        when (deviceType) {
                            (DeviceType.ALARME.type) -> {
                                val createdDevice = AlarmDevice(
                                    id = "",
                                    name = newName,
                                    macAddress = newMacAddress,
                                    password = newPassword,
                                    type = null
                                )

                                lifecycleScope.launch(Dispatchers.IO) {
                                    val returnBackend = webClient.addDevice(createdDevice)
                                    AddDeviceDialog.newInstance(returnBackend)
                                }
                            }

                            (DeviceType.VÍDEO.type) -> {
                                val createdDevice = VideoDevice(
                                    id = "",
                                    name = newName,
                                    serial = newSerialNumber,
                                    username = newUser,
                                    password = newPassword,
                                    type = null
                                )

                                lifecycleScope.launch(Dispatchers.IO) {
                                    val returnBackend = webClient.addDevice(createdDevice)
                                    AddDeviceDialog.newInstance(returnBackend)
                                }
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun changeLayoutVisibility() {

        when (deviceType) {
            (DeviceType.ALARME.type) -> {
                showAlarmDeviceInfo()
                hideVideoDeviceInfo()
            }

            (DeviceType.VÍDEO.type) -> {
                showVideoDeviceInfo()
                hideAlarmDeviceInfo()
            }
        }
    }

    private fun readDeviceTypeInput() {
        with(binding.addAutoComplete) {
            val deviceTypesArray = resources.getStringArray(R.array.device_types)

            setAdapter(ArrayAdapter(this@AddDeviceActivity, android.R.layout.simple_dropdown_item_1line, deviceTypesArray))

            onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
                deviceType = parent.getItemAtPosition(position) as String
                changeLayoutVisibility()
            }
        }
    }

    private fun showVideoDeviceInfo() {
        binding.addDeviceName.visibility = View.VISIBLE
        binding.addDeviceSerialNumber.visibility = View.VISIBLE
        binding.addDeviceUser.visibility = View.VISIBLE
        binding.addDevicePassword.visibility = View.VISIBLE
    }

    private fun hideVideoDeviceInfo() {
        binding.addDeviceUser.visibility = View.GONE
        binding.addDeviceSerialNumber.visibility = View.GONE
    }

    private fun showAlarmDeviceInfo() {
        binding.addDeviceName.visibility = View.VISIBLE
        binding.addDeviceMacAddress.visibility = View.VISIBLE
        binding.addDevicePassword.visibility = View.VISIBLE
    }

    private fun hideAlarmDeviceInfo() {
        binding.addDeviceMacAddress.visibility = View.GONE

    }

}
