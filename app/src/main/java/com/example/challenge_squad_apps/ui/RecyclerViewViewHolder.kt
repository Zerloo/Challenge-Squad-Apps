package com.example.challenge_squad_apps.ui

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.DeviceListItemBinding
import com.example.challenge_squad_apps.webclient.models.AlarmDevice
import com.example.challenge_squad_apps.webclient.models.Device

class RecyclerViewViewHolder(private val binding: DeviceListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceType")
    fun bind(device: Device, listener: RecyclerViewListener) {

        with(binding) {
            deviceNameTextView.text = device.name

            if (device is AlarmDevice){
                deviceIconImageView.setImageResource(R.drawable.icalarmdevicebottom)
            } else {
                deviceIconImageView.setImageResource(R.drawable.icvideodevicebottom)
            }
            deviceDropdownImageView.setOnClickListener{listener.onDropdownPressed(this.deviceDropdownImageView, device)}
        }
    }
}

