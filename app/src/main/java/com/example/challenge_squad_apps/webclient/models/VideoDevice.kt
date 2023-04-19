package com.example.challenge_squad_apps.webclient.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
class VideoDevice(
    id: String = UUID.randomUUID().toString(),
    name: String,
    val serial: String,
    val username: String,
    password: String,
    type: String,
    favorite: String,
) : Device(id, name, password, type, favorite)
