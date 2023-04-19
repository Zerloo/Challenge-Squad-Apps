package com.example.challenge_squad_apps.webclient.models

import androidx.room.Entity
import java.util.UUID


@Entity
class AlarmDevice(
    val macAddress: String,
    id: String = UUID.randomUUID().toString(),
    name: String,
    password: String,
    type: String,
    favorite: String,
) : Device(id, name, password, type, favorite)

