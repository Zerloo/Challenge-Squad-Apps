package com.example.challenge_squad_apps.ui.view_models

import androidx.lifecycle.ViewModel
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.WebClient

class EditDeviceViewModel : ViewModel() {

    private val webClient by lazy { WebClient() }

    suspend fun saveDeviceChanges(deviceId: String?, deviceType: String?, newDeviceName: String?, newDeviceUsername: String?, newDevicePassword: String?): Boolean {

        return if (deviceType == DeviceType.ALARM.type) {
            webClient.editAlarm(deviceId.toString(), newDeviceName, newDevicePassword)

        } else {
            webClient.editVideo(deviceId.toString(), newDeviceName, newDeviceUsername, newDevicePassword)
        }
    }
}
