package com.example.challenge_squad_apps.webclient.models

import androidx.room.Entity

@Entity
class VideoDevice(
    id: String,
    name: String,
    var serial: String,
    var username: String,
    password: String,
    type: String,
    favorite: String,
) : Device(id, name, password, type, favorite)
