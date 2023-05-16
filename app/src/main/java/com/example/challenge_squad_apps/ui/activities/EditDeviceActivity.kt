package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.EditDeviceBinding
import com.example.challenge_squad_apps.ui.utils.dialogs.EditDeviceDialog
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.ui.view_models.EditDeviceViewModel
import kotlinx.coroutines.launch

class EditDeviceActivity : AppCompatActivity(), EditDeviceDialog.EditDeviceDialogListener {

    private lateinit var binding: EditDeviceBinding
    private lateinit var editViewModel: EditDeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EditDeviceBinding.inflate(layoutInflater)
        editViewModel = ViewModelProvider(this)[EditDeviceViewModel::class.java]
        setContentView(binding.root)

        setupView()
    }

    override fun onResume() {
        super.onResume()
        onBackButtonPressed()
        saveChanges()
    }


    private fun saveChanges() {
        with(binding) {
            editDeviceTopAppBar.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.save_menu) {

                    val extras = intent.extras
                    val deviceId = extras?.getString("id")
                    val deviceType = extras?.getString("Type")
                    val newDeviceName: String? = inputEditDeviceName.text.toString()
                    val newDeviceUsername: String? = inputEditDeviceUser.text.toString()
                    val newDevicePassword: String? = inputEditDevicePassword.text.toString()

                    lifecycleScope.launch {
                        val returnBackend = editViewModel.saveDeviceChanges(deviceId, deviceType, newDeviceName, newDeviceUsername, newDevicePassword)
                        val dialog = EditDeviceDialog.newInstance(returnBackend)
                        dialog.show(supportFragmentManager, EditDeviceDialog.TAG)
                    }
                    true
                } else {
                    false
                }
            }
        }
    }


    private fun setupView() {
        with(binding) {

            val extras = intent.extras
            val rastreability = extras?.getString("Rastreability")

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
        binding.editDeviceTopAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun confirmButtonClicked() {
        this@EditDeviceActivity.finish()
    }
}
