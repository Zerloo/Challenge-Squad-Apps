package com.example.challenge_squad_apps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge_squad_apps.webclient.models.Device

class DeviceListAdapter(private val deviceList: List<Device>) :
    RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.deviceNameTextView)
        val iconImageView: ImageView = itemView.findViewById(R.id.deviceIconImageView)
        val dropdownMenuButton: ImageButton = itemView.findViewById(R.id.deviceDropdownImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_list_item, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceList[position]
        when (device.type) {
            "Video" -> {
                holder.iconImageView.setImageResource(R.drawable.icvideodevicebottom)
            }

            "Alarm" -> {
                holder.iconImageView.setImageResource(R.drawable.icalarmdevicebottom)
            }

            else -> {
                holder.iconImageView.setImageResource(R.drawable.icerror)
            }
        }
        holder.nameTextView.text = device.name
        holder.dropdownMenuButton.setOnClickListener { view ->
            showDropdownMenu(view, device)
        }
    }

    override fun getItemCount(): Int = deviceList.size
}

private fun showDropdownMenu(view: View, device: Device) {
    val popup = PopupMenu(view.context, view)
    popup.menuInflater.inflate(R.menu.dropdown_menu, popup.menu)


    popup.setOnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.editMenuItem -> {
                true
            }

            R.id.infoMenuItem -> {
                true
            }

            R.id.unfavoriteMenuItem -> {
                true
            }

            R.id.favoriteMenuItem -> {
                true
            }

            R.id.deleteMenuItem -> {
                true
            }

            else -> false
        }
    }
    popup.show()
}