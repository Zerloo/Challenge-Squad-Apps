package com.example.challenge_squad_apps.ui.webclient.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
open class Device(
    @PrimaryKey
    open val id: String = UUID.randomUUID().toString(),
    open val name: String,
    open val password: String,
    open val type: String,
    open var favorite: String,
)
