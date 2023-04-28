package com.example.challenge_squad_apps.webclient

class DeleteDevice {
    private val webClient by lazy {
        WebClient()
    }

    suspend fun deleteDeviceAlarm(deviceID: String) {
        webClient.deleteAlarm(deviceID)
    }

    suspend fun deleteDeviceVideo(deviceID: String) {
        webClient.deleteVideo(deviceID)
    }
}

