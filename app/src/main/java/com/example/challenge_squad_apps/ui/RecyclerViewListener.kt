package com.example.challenge_squad_apps.ui

import android.view.View
import com.example.challenge_squad_apps.webclient.models.Device

interface RecyclerViewListener {
    fun onDropdownPressed (view: View, device: Device)
}