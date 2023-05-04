package com.example.challenge_squad_apps.webclient.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmDevice(
    @PrimaryKey
    override val id: String,
    val macAddress: String,
    override var name: String,
    override var password: String,
    override val type: String,
) : Device(id, name, password, type)

