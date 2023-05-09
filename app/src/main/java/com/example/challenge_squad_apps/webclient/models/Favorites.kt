package com.example.challenge_squad_apps.webclient.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.challenge_squad_apps.dao.FavoritesDao

@Entity
data class Favorites(
    @PrimaryKey
    @ColumnInfo("id") val id: String,
)