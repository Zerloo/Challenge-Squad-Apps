package com.example.challenge_squad_apps.ui

import androidx.appcompat.widget.PopupMenu
import com.example.challenge_squad_apps.webclient.models.Device

interface DropdownMenuListener {
    fun onMenuItemPressed(popup: PopupMenu, device: Device)
}
