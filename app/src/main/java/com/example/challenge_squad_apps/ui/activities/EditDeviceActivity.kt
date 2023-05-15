package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.EditDeviceBinding
import com.example.challenge_squad_apps.ui.utils.dialogs.EditDeviceDialog
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.DeviceType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditDeviceActivity : AppCompatActivity() {

    private lateinit var binding: EditDeviceBinding
    private val webClient by lazy { WebClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    override fun onResume() {
        super.onResume()

        onBackButtonPressed()
        saveActivity()
    }


    private fun saveActivity() {
        with(binding) {

            val extras = intent.extras
            val deviceType = extras?.getString("Type")
            val deviceID = extras?.getString("id")

            val newDeviceName: String? = inputEditDeviceName.text.toString()
            val newDeviceUsername: String? = inputEditDeviceUser.text.toString()
            val newDevicePassword: String? = inputEditDevicePassword.text.toString()

            editDeviceTopAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save_menu -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            when (deviceType) {
                                (DeviceType.ALARM.type) -> {
                                    val returnBackend = webClient.editAlarm(deviceID.toString(), newDeviceName, newDevicePassword)
                                    EditDeviceDialog.newInstance(returnBackend)
                                }

                                (DeviceType.VIDEO.type) -> {
                                    val returnBackend = webClient.editVideo(deviceID.toString(), newDeviceName, newDeviceUsername, newDevicePassword)
                                    EditDeviceDialog.newInstance(returnBackend)
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


    private fun setupView() {
        with(binding) {

            val extras = intent.extras
            val rastreability = extras?.getString("rastreability")

            when (extras?.getString("Type")) {
                (DeviceType.ALARM.type) -> {
                    inputEditDeviceMacAddress.setText(rastreability)
                    inputEditDeviceMacAddress.isEnabled = false
                    inputEditDeviceSerialNumber.visibility = View.GONE
                    inputEditDeviceUser.visibility = View.GONE
                }

                (DeviceType.VIDEO.type) -> {
                    inputEditDeviceSerialNumber.setText(rastreability)
                    inputEditDeviceSerialNumber.isEnabled = false
                    inputEditDeviceMacAddress.visibility = View.GONE
                }
            }
        }
    }

    private fun onBackButtonPressed() {
        binding.editDeviceTopAppBar.setNavigationOnClickListener() {
            finish()
        }
    }
}
