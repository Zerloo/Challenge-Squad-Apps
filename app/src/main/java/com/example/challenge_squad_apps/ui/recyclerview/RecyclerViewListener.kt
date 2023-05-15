package com.example.challenge_squad_apps.ui.recyclerview

import android.view.View
import com.example.challenge_squad_apps.webclient.dto.models.Device

interface RecyclerViewListener {
    fun onDropdownPressed (view: View, device: Device)
}