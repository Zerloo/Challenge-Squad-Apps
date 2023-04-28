package com.example.challenge_squad_apps.webclient.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EditDevice(
    var name: String,
    var username: String?,
    var password: String,
)