package com.example.challenge_squad_apps.ui.activities

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.ui.webclient.models.AlarmDevice
import com.example.challenge_squad_apps.ui.webclient.models.Device
import com.example.challenge_squad_apps.ui.webclient.models.VideoDevice

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
                val intent = Intent(view.context, EditActivity::class.java)
                intent.putExtra("Type", device.type)
                view.context.startActivity(intent)
                true
            }

            R.id.infoMenuItem -> {
                val intent = Intent(view.context, InfoActivity::class.java)
                intent.putExtra("Type", device.type)

                if (device is AlarmDevice) {
                    intent.putExtra("Mac Address", device.macAddress)
                } else if (device is VideoDevice){
                    intent.putExtra("Serial", device.serial)
                    intent.putExtra("Username", device.username)
                }

                view.context.startActivity(intent)
                true
            }

            R.id.unfavoriteMenuItem -> {
                device.favorite = "false"
                true
            }

            R.id.favoriteMenuItem -> {
                device.favorite = "true"
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