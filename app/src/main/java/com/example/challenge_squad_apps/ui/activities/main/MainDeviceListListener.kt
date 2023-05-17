package com.example.challenge_squad_apps.ui.activities.main

import android.view.View
import com.example.challenge_squad_apps.webclient.dto.models.Device

interface MainDeviceListListener {
    fun onDropdownPressed (view: View, device: Device)
}