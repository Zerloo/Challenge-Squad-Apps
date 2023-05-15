package com.example.challenge_squad_apps.webclient.dto.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddDevice(
    var name: String,
    var serial: String?,
    var username: String?,
    var macAddress: String?,
    var password: String,
)