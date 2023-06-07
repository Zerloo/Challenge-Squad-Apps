package com.example.challenge_squad_apps.ui.activities.addDevice

import android.app.Activity
import android.bluetooth.BluetoothClass.Device
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.AddDeviceBinding
import com.example.challenge_squad_apps.ui.utils.dialogs.AddDeviceDialog
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice

class AddDeviceActivity : AppCompatActivity(), AddDeviceDialog.AddDeviceDialogListener {

    private lateinit var binding: AddDeviceBinding
    private lateinit var addViewModel: AddDeviceViewModel
    private lateinit var deviceAddReturn: com.example.challenge_squad_apps.webclient.dto.models.Device
    private var deviceType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addViewModel = ViewModelProvider(this)[AddDeviceViewModel::class.java]

        setupListeners()
        registerObservers()
    }

    override fun onResume() {
        super.onResume()
        readDeviceTypeInput()
    }

    private fun registerObservers() {
        addViewModel.addDeviceLiveData.observe(this) { backendResponse ->
            deviceAddReturn = backendResponse
            showAddDeviceDialog(201)
        }
        addViewModel.addDeviceErrorLiveData.observe(this) { backendResponse ->
            showAddDeviceDialog(backendResponse)
        }
    }

    private fun showAddDeviceDialog(backendResponse: Int) {
        val dialog = AddDeviceDialog.newInstance(backendResponse)
        dialog.show(supportFragmentManager, AddDeviceDialog.TAG)
    }

    private fun setupListeners() {
        with(binding) {

            addDeviceTopAppBar.setNavigationOnClickListener {
                finish()
            }

            addDeviceTopAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save_menu -> {
                        if (deviceType == DeviceType.ALARME.type) {
                            val name = inputAddDeviceName.text.toString()
                            val macAddress: String = inputAddDeviceMacAddress.text.toString()
                            val password = inputAddDevicePassword.text.toString()
                            addViewModel.addAlarmDevice(name, macAddress, password)
                        } else {
                            val name = inputAddDeviceName.text.toString()
                            val serialNumber: String = inputAddDeviceSerialNumber.text.toString()
                            val user: String = inputAddDeviceUser.text.toString()
                            val password = inputAddDevicePassword.text.toString()
                            addViewModel.addVideoDevice(name, serialNumber, user, password)
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
        val resultIntent = Intent()
        resultIntent.putExtra("device", deviceAddReturn)
        setResult(Activity.RESULT_OK, resultIntent)
        this@AddDeviceActivity.finish()
    }
}
