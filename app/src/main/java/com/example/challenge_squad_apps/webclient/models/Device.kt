package com.example.challenge_squad_apps.webclient.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Device(
    @PrimaryKey
    open val id: String,
    open var name: String,
    open var password: String,
    open val type: String,
    open var favorite: String,
)
