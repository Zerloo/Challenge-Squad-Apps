package com.example.challenge_squad_apps.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_squad_apps.ui.utils.dialogs.AddDeviceDialog
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType
import com.example.challenge_squad_apps.webclient.WebClient
import com.example.challenge_squad_apps.webclient.dto.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.dto.models.VideoDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddDeviceViewModel : ViewModel() {
    private val webClient by lazy { WebClient() }


    suspend fun addDevice(deviceType: String, name: String, serialNumber: String?, user: String?, macAddress: String?, password: String): Boolean {
        if (deviceType == DeviceType.ALARME.type) {
            val createdDevice = AlarmDevice(
                id = "0",
                name = name,
                macAddress = macAddress,
                password = password,
            )

            return webClient.addDevice(createdDevice)

        } else {
            val createdDevice = VideoDevice(
                id = "0",
                name = name,
                serial = serialNumber,
                username = user,
                password = password,
            )

            return webClient.addDevice(createdDevice)
        }
    }
}