package com.example.challenge_squad_apps.webclient

import android.content.Context
import com.example.challenge_squad_apps.webclient.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.models.Device
import com.example.challenge_squad_apps.webclient.models.Dialog
import com.example.challenge_squad_apps.webclient.models.VideoDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeleteDevice {
    private val webClient by lazy {
        WebClient()
    }

    fun deleteDropdownMenuDialog(
        context: Context,
        device: Device,
    ): Boolean {
        val deleteDialog = Dialog()
        var choose: Boolean = true
        deleteDialog.showDialog(
            context,
            "Remover dispositivo?",
            "Você tem certeza que deseja remover este dispositivo?",
            "Confirmar",
            "Cancelar",
            positiveAction = {
                GlobalScope.launch(Dispatchers.IO) {
                    if (device is AlarmDevice) {
                        deleteDeviceAlarm(device.id)

                    } else if (device is VideoDevice) {
                        deleteDeviceVideo(device.id)
                    }
                }
                choose = true
            },
            negativeAction = {
                choose = false
            }
        )
        return choose
    }

    suspend fun deleteDeviceAlarm(deviceID: String): Boolean {
        return webClient.deleteAlarm(deviceID)
    }

    suspend fun deleteDeviceVideo(deviceID: String): Boolean {
        return webClient.deleteVideo(deviceID)
    }
}



