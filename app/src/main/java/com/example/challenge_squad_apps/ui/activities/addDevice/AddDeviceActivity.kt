package com.example.challenge_squad_apps.ui.activities.addDevice

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.AddDeviceBinding
import com.example.challenge_squad_apps.ui.utils.dialogs.AddDeviceDialog
import com.example.challenge_squad_apps.ui.utils.dialogs.EditDeviceDialog
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import kotlinx.coroutines.launch

class AddDeviceActivity : AppCompatActivity(), AddDeviceDialog.AddDeviceDialogListener {

    private lateinit var binding: AddDeviceBinding
    private lateinit var addViewModel: AddDeviceViewModel
    private var deviceType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddDeviceBinding.inflate(layoutInflater)
        addViewModel = ViewModelProvider(this)[AddDeviceViewModel::class.java]
        setContentView(binding.root)

        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        readDeviceTypeInput()
    }

    private fun setupListeners() {
        with(binding) {

            addDeviceTopAppBar.setNavigationOnClickListener {
                finish()
            }
            addDeviceTopAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save_menu -> {

                        val name = inputAddDeviceName.text.toString()
                        val serialNumber: String? = inputAddDeviceSerialNumber.text.toString()
                        val user: String? = inputAddDeviceUser.text.toString()
                        val macAddress: String? = inputAddDeviceMacAddress.text.toString()
                        val password = inputAddDevicePassword.text.toString()

                        lifecycleScope.launch {
                            val returnBackend = addViewModel.addDevice(deviceType, name, serialNumber, user, macAddress, password)
                            val dialog = AddDeviceDialog.newInstance(returnBackend)
                            dialog.show(supportFragmentManager, AddDeviceDialog.TAG)
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun changeLayoutVisibility() {
        with(binding.addDeviceType) {
            when (deviceType) {
                (DeviceType.ALARME.type) -> {
                    showAlarmDeviceInfo()
                    hideVideoDeviceInfo()
                    isHelperTextEnabled = false
                }

                (DeviceType.VÍDEO.type) -> {
                    showVideoDeviceInfo()
                    hideAlarmDeviceInfo()
                    isHelperTextEnabled = false
                }
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
        with(binding) {
            addDeviceName.visibility = View.VISIBLE
            addDeviceSerialNumber.visibility = View.VISIBLE
            addDeviceUser.visibility = View.VISIBLE
            addDevicePassword.visibility = View.VISIBLE
        }
    }

    private fun hideVideoDeviceInfo() {
        with(binding) {
            addDeviceUser.visibility = View.GONE
            addDeviceSerialNumber.visibility = View.GONE
        }
    }

    private fun showAlarmDeviceInfo() {
        with(binding) {
            addDeviceName.visibility = View.VISIBLE
            addDeviceMacAddress.visibility = View.VISIBLE
            addDevicePassword.visibility = View.VISIBLE
        }
    }

    private fun hideAlarmDeviceInfo() {
        binding.addDeviceMacAddress.visibility = View.GONE
    }

    override fun confirmButtonClicked() {
        this@AddDeviceActivity.finish()
    }

}