package com.example.challenge_squad_apps.ui.activities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.models.Dialog
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    private lateinit var topAppBar: MaterialToolbar
    private val webClient by lazy {
        WebClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEditView()
    }

    override fun onResume() {
        super.onResume()
        val extras = intent.extras
        val deviceType = extras?.getString("Type")
        val deviceID = extras?.getString("id")

        returnActivity()
        saveActivity(deviceType, deviceID)
    }


    private fun saveActivity(deviceType: String?, deviceID: String?) {
        this.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save_menu -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        when (deviceType) {
                            ("Alarm") -> {
                                val newDeviceName = findViewById<TextView>(R.id.input_edit_alarm_device_name).text.toString()
                                val newDevicePassword = findViewById<TextView>(R.id.input_edit_alarm_device_password).text.toString()
                                val returnBackend = webClient.editAlarm(deviceID.toString(), newDeviceName, newDevicePassword)

                                ContextCompat.getMainExecutor(this@EditActivity).execute {
                                    confirmationDialog(returnBackend, this@EditActivity)
                                }
                            }

                            ("Video") -> {
                                val newDeviceName = findViewById<TextView>(R.id.input_edit_video_device_name).text.toString()
                                val newDeviceUsername = findViewById<TextView>(R.id.input_edit_video_device_user).text.toString()
                                val newDevicePassword = findViewById<TextView>(R.id.input_edit_video_device_password).text.toString()
                                val returnBackend = webClient.editVideo(deviceID.toString(), newDeviceName, newDeviceUsername, newDevicePassword)

                                ContextCompat.getMainExecutor(this@EditActivity).execute {
                                    confirmationDialog(returnBackend, this@EditActivity)
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

    private fun setEditView() {
        val extras = intent.extras
        val  rastreability= extras?.getString("rastreability")
        when (extras?.getString("Type")) {

            ("Alarm") -> {
                setContentView(R.layout.edit_alarm_device)
                findViewById<TextInputEditText>(R.id.input_edit_alarm_device_mac_address).setText(rastreability)
                this.topAppBar = findViewById(R.id.edit_alarm_device_topAppBar)
            }

            ("Video") -> {
                setContentView(R.layout.edit_video_device)
                findViewById<TextInputEditText>(R.id.input_edit_video_device_serial_number).setText(rastreability)
                this.topAppBar = findViewById(R.id.edit_video_device_topAppBar)
            }

        }
    }

    private fun returnActivity() {
        this.topAppBar.setNavigationOnClickListener() {
            finish()
        }
    }

    private fun confirmationDialog(returnBackend: Boolean, context: Context) {
        val confirmationDialog = Dialog()

        val message: String = if (returnBackend) {
            "Dispositivo editado com sucesso"
        } else {
            "Falha ao atualizar informações do dispositivo"
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
