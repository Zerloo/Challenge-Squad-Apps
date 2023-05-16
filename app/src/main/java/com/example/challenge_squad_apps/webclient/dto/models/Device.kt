package com.example.challenge_squad_apps.webclient.dto.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
abstract class Device(
    @PrimaryKey
    open val id: String,
    open var name: String,
    open var password: String,
)
