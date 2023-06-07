package com.example.challenge_squad_apps.webclient.dto.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class AlarmDevice(
    @PrimaryKey
    override val id: String,
    override var name: String,
    val macAddress: String?,
    override var password: String,
) : Device(id, name, password), Parcelable

