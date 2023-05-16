package com.example.challenge_squad_apps.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorites(
    @PrimaryKey
    @ColumnInfo("id") val id: String,
)