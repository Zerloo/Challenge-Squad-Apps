package com.example.challenge_squad_apps.webclient.dto.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
abstract class Device(
    @PrimaryKey
    @Transient
    open val id: String,
    @Transient
    open var name: String,
    @Transient
    open var password: String,
)
