package com.example.challenge_squad_apps.webclient.dto.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class AlarmDevice(
    @PrimaryKey
    override val id: String,
    override var name: String,
    val macAddress: String?,
    override var password: String,
) : Device(id, name, password)

