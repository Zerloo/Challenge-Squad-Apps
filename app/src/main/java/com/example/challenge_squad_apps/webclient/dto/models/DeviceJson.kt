package com.example.challenge_squad_apps.webclient.dto.models

data class VideoDeviceJson(
    var id: String,
    var name: String,
    var serial: String,
    var username: String,
    var password: String,
)

data class AlarmDeviceJson(
    val id: String,
    val name: String,
    val macAddress: String,
    val password: String,
)