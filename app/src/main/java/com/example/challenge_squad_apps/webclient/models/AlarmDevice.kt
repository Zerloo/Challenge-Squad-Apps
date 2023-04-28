package com.example.challenge_squad_apps.webclient.models

import androidx.room.Entity

@Entity
class AlarmDevice(
    val macAddress: String,
    id: String,
    name: String,
    password: String,
    type: String,
    favorite: String,
) : Device(id, name, password, type, favorite)

