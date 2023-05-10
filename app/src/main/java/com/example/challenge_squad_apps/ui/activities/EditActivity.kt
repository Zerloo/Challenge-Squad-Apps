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
import com.example.challenge_squad_apps.databinding.EditAlarmDeviceBinding
import com.example.challenge_squad_apps.databinding.EditVideoDeviceBinding
import com.example.challenge_squad_apps.databinding.MainActivityBinding
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.models.Dialog
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    private lateinit var bindingVideo: EditVideoDeviceBinding
    private lateinit var bindingAlarm: EditAlarmDeviceBinding

    private lateinit var topAppBar: MaterialToolbar
    private val webClient by lazy { WebClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingAlarm = EditAlarmDeviceBinding.inflate(layoutInflater)
        bindingVideo = EditVideoDeviceBinding.inflate(layoutInflater)

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
                                val newDeviceName = bindingAlarm.inputEditAlarmDeviceName.text.toString()
                                val newDevicePassword = bindingAlarm.inputEditAlarmDevicePassword.text.toString()
                                val returnBackend = webClient.editAlarm(deviceID.toString(), newDeviceName, newDevicePassword)

                                ContextCompat.getMainExecutor(this@EditActivity).execute {
                                    confirmationDialog(returnBackend, this@EditActivity)
                                }
                            }

                            ("Video") -> {
                                val newDeviceName = bindingVideo.inputEditVideoDeviceName.text.toString()
                                val newDeviceUsername = bindingVideo.inputEditVideoDeviceUser.text.toString()
                                val newDevicePassword = bindingVideo.inputEditVideoDevicePassword.text.toString()
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
        val rastreability = extras?.getString("rastreability")
        when (extras?.getString("Type")) {

            ("Alarm") -> {
                setContentView(bindingAlarm.root)
                bindingAlarm.inputEditAlarmDeviceMacAddress.setText(rastreability)
                bindingAlarm.inputEditAlarmDeviceMacAddress.isEnabled = false
                this.topAppBar = bindingAlarm.editAlarmDeviceTopAppBar
            }

            ("Video") -> {
                setContentView(bindingVideo.root)
                bindingVideo.inputEditVideoDeviceSerialNumber.setText(rastreability)
                bindingVideo.inputEditVideoDeviceSerialNumber.isEnabled = false
                this.topAppBar = bindingVideo.editVideoDeviceTopAppBar
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
