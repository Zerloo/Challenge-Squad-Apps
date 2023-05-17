package com.example.challenge_squad_apps.ui.activities.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.challenge_squad_apps.databinding.DeviceListItemBinding
import com.example.challenge_squad_apps.webclient.dto.models.Device


class MainDeviceListAdapter(private val listener: MainDeviceListListener) : ListAdapter<Device, MainDeviceListViewHolder>(MainDeviceListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainDeviceListViewHolder {
        val binding = DeviceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainDeviceListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainDeviceListViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    private companion object : DiffUtil.ItemCallback<Device>() {
        override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem == newItem
        }
    }
}
