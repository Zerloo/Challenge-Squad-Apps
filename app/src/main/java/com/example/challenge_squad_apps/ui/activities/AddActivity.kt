package com.example.challenge_squad_apps.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.AddDeviceBinding
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.models.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    private lateinit var binding: AddDeviceBinding
    private var globalDeviceType = ""
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
        saveDevice()
        returnActivity()
    }

    private fun saveDevice() {
        binding.addDeviceTopAppBar.setOnMenuItemClickListener { menuItem ->
            menuItem.itemId
            when (menuItem.itemId) {
                R.id.save_menu -> {

                    val newName = binding.inputAddDeviceName.text.toString()
                    var newSerialNumber: String? = binding.inputAddDeviceSerialNumber.text.toString()
                    var newUser: String? = binding.inputAddDeviceUser.text.toString()
                    var newMacAddress: String? = binding.inputAddDeviceMacAddress.text.toString()
                    val newPassword = binding.inputAddDevicePassword.text.toString()

                    when (globalDeviceType) {
                        ("Alarme") -> {
                            newSerialNumber = null
                            newUser = null
                            lifecycleScope.launch(Dispatchers.IO) {
                                val returnBackend = webClient.addDevice(newName, newSerialNumber, newUser, newMacAddress, newPassword, globalDeviceType)
                                ContextCompat.getMainExecutor(this@AddActivity).execute {
                                    confirmationDialog(returnBackend, this@AddActivity)
                                }
                            }
                        }

                        ("Vídeo") -> {
                            newMacAddress = null
                            lifecycleScope.launch(Dispatchers.IO) {
                                val returnBackend = webClient.addDevice(newName, newSerialNumber, newUser, newMacAddress, newPassword, globalDeviceType)
                                ContextCompat.getMainExecutor(this@AddActivity).execute {
                                    confirmationDialog(returnBackend, this@AddActivity)
                                }
                            }
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

    private fun returnActivity() {
        binding.addDeviceTopAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun changeLayoutVisibility() {

        when (globalDeviceType) {
            ("Alarme") -> {
                binding.addDeviceUser.visibility = View.GONE
                binding.addDeviceSerialNumber.visibility = View.GONE
                binding.addDeviceMacAddress.visibility = View.VISIBLE
                binding.addDevicePassword.visibility = View.VISIBLE
                binding.addDeviceName.visibility = View.VISIBLE
            }

            ("Vídeo") -> {
                binding.addDeviceUser.visibility = View.VISIBLE
                binding.addDeviceSerialNumber.visibility = View.VISIBLE
                binding.addDeviceMacAddress.visibility = View.GONE
                binding.addDevicePassword.visibility = View.VISIBLE
                binding.addDeviceName.visibility = View.VISIBLE
            }

            ("") -> {
                binding.addDeviceUser.visibility = View.GONE
                binding.addDeviceSerialNumber.visibility = View.GONE
                binding.addDeviceMacAddress.visibility = View.GONE
                binding.addDevicePassword.visibility = View.VISIBLE
                binding.addDeviceName.visibility = View.VISIBLE
            }
        }
    }

    private fun readDeviceTypeInput() {

        val deviceTypesArray = resources.getStringArray(R.array.device_types)

        binding.addAutoComplete.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, deviceTypesArray))
        binding.addAutoComplete.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            globalDeviceType = parent.getItemAtPosition(position) as String
            changeLayoutVisibility()
        }
    }

    private fun confirmationDialog(returnBackend: Boolean, context: Context) {
        val confirmationDialog = Dialog()

        val message: String = if (returnBackend) {
            "Dispositivo cadastrado com sucesso"
        } else {
            "Falha ao cadastrar informações do dispositivo"
        }

        confirmationDialog.showDialog(
            context,
            "",
            message,
            "Ok",
            negativeMessage = "",
            positiveAction = {
                finish()
            },
            negativeAction = {}
        )
    }
}



