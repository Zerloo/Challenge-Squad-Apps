package com.example.challenge_squad_apps.ui.activities.editDevice

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.EditDeviceBinding
import com.example.challenge_squad_apps.ui.utils.dialogs.EditDeviceDialog
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType

class EditDeviceActivity : AppCompatActivity(), EditDeviceDialog.EditDeviceDialogListener {

    private lateinit var binding: EditDeviceBinding
    private lateinit var editViewModel: EditDeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EditDeviceBinding.inflate(layoutInflater)
        editViewModel = ViewModelProvider(this)[EditDeviceViewModel::class.java]
        setContentView(binding.root)

        setupView()
        registerObservers()
    }

    override fun onResume() {
        super.onResume()
        onBackButtonPressed()
        saveChanges()
    }

    private fun registerObservers() {
        editViewModel.editDeviceLiveData.observe(this) { responseStatus ->
            showAddDeviceDialog(responseStatus)
        }

        editViewModel.editDeviceErrorLiveData.observe(this) { error ->
            showErrorToast(error)
        }
    }
    private fun showErrorToast(errorMessage: String?) {
        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }
    private fun showAddDeviceDialog(showDialog: Boolean) {
        val dialog = EditDeviceDialog.newInstance(showDialog)
        dialog.show(supportFragmentManager, EditDeviceDialog.TAG)
    }

    private fun saveChanges() {
        with(binding) {
            editDeviceTopAppBar.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.save_menu) {

                    val extras = intent.extras
                    val deviceId = extras?.getString("id")
                    val deviceType = extras?.getString("type")
                    val newDeviceName: String? = inputEditDeviceName.text.toString()
                    val newDeviceUsername: String? = inputEditDeviceUser.text.toString()
                    val newDevicePassword: String? = inputEditDevicePassword.text.toString()

                    editViewModel.saveDeviceChanges(deviceId, deviceType, newDeviceName, newDeviceUsername, newDevicePassword)

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
            val name = extras?.getString("name")
            val rastreability = extras?.getString("rastreability")
            val password = extras?.getString("password")

            editDeviceName.visibility = View.VISIBLE
            editDevicePassword.visibility = View.VISIBLE

            inputEditDeviceName.setText(name)
            inputEditDevicePassword.setText(password)

            when (extras?.getString("type")) {
                (DeviceType.ALARM.type) -> {
                    inputEditDeviceMacAddress.isEnabled = false
                    editDeviceMacAddress.visibility = View.VISIBLE

                    inputEditDeviceMacAddress.setText(rastreability)
                }

                (DeviceType.VIDEO.type) -> {
                    inputEditDeviceSerialNumber.isEnabled = false
                    editDeviceSerialNumber.visibility = View.VISIBLE
                    editDeviceUser.visibility = View.VISIBLE

                    inputEditDeviceUser.setText(extras?.getString("username"))
                    inputEditDeviceSerialNumber.setText(rastreability)
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
