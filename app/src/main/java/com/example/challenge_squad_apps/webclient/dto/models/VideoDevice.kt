package com.example.challenge_squad_apps.webclient.dto.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class VideoDevice(
    @PrimaryKey
    override val id: String,
    override var name: String,
    var serial: String?,
    var username: String?,
    override var password: String,
) : Device(id, name, password), Parcelable
