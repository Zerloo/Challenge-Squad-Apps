package com.example.challenge_squad_apps.webclient.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorites(
    @PrimaryKey
    val id: String,
)