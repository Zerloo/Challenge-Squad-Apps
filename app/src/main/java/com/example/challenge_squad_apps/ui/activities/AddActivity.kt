package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.webclient.WebClient
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    private lateinit var topAppBar: MaterialToolbar
    private var globalDeviceType = ""
    private val webClient by lazy {
        WebClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_device)
        topAppBar = findViewById(R.id.add_device_topAppBar)
    }

    override fun onResume() {
        super.onResume()

        readDeviceTypeInput()
        saveDevice()
        returnActivity()
    }

    private fun saveDevice() {
        this.topAppBar.setOnMenuItemClickListener { menuItem ->
            menuItem.itemId
            when (menuItem.itemId) {
                R.id.save_menu -> {

                    val newName = findViewById<TextView>(R.id.input_add_device_name).text.toString()
                    var newSerialNumber: String? = findViewById<TextView>(R.id.input_add_device_serial_number).text.toString()
                    var newUser: String? = findViewById<TextView>(R.id.input_add_device_user).text.toString()
                    var newMacAddress: String? = findViewById<TextView>(R.id.input_add_device_mac_address).text.toString()
                    val newPassword = findViewById<TextView>(R.id.input_add_device_password).text.toString()

                    when (globalDeviceType) {
                        ("Alarme") -> {
                            newSerialNumber = null
                            newUser = null
                            GlobalScope.launch(Dispatchers.IO) {
                                webClient.addDevice(newName, newSerialNumber, newUser, newMacAddress, newPassword, globalDeviceType)
                            }
                        }

                        ("Vídeo") -> {
                            newMacAddress = null
                            lifecycleScope.launch(Dispatchers.IO) {
                                webClient.addDevice(newName, newSerialNumber, newUser, newMacAddress, newPassword, globalDeviceType)
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
        topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun changeLayoutVisibility() {

        val name = findViewById<TextInputLayout>(R.id.add_device_name)
        val serialNumber = findViewById<TextInputLayout>(R.id.add_device_serial_number)
        val user = findViewById<TextInputLayout>(R.id.add_device_user)
        val macAddress = findViewById<TextInputLayout>(R.id.add_device_mac_address)
        val password = findViewById<TextInputLayout>(R.id.add_device_password)

        when (globalDeviceType) {
            ("Alarme") -> {
                user.visibility = View.GONE
                serialNumber.visibility = View.GONE
                macAddress.visibility = View.VISIBLE
                password.visibility = View.VISIBLE
                name.visibility = View.VISIBLE
            }

            ("Vídeo") -> {
                user.visibility = View.VISIBLE
                serialNumber.visibility = View.VISIBLE
                macAddress.visibility = View.GONE
                password.visibility = View.VISIBLE
                name.visibility = View.VISIBLE
            }

            ("") -> {
                user.visibility = View.GONE
                serialNumber.visibility = View.GONE
                macAddress.visibility = View.GONE
                password.visibility = View.VISIBLE
                name.visibility = View.VISIBLE
            }
        }


    }

    private fun readDeviceTypeInput() {

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.add_auto_complete)
        val deviceTypesArray = resources.getStringArray(R.array.device_types)
        autoCompleteTextView.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, deviceTypesArray))
        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            globalDeviceType = parent.getItemAtPosition(position) as String
            changeLayoutVisibility()
        }
    }
}


