package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge_squad_apps.R
import com.google.android.material.appbar.MaterialToolbar

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        val deviceType = extras?.getString("Type")
        when (deviceType) {
            ("Alarm") -> {
                setContentView(R.layout.info_alarm_device)
                val topAppBar: MaterialToolbar = findViewById(R.id.info_alarm_device_topAppBar)
                topAppBar.setNavigationOnClickListener() {
                    finish()
                }

                val type = findViewById<TextView>(R.id.fill_info_alarm_device_type)
                type.text = "Alarme"

                val deviceMacAddress = extras.getString("Mac Address")
                val macAddress = findViewById<TextView>(R.id.fill_info_alarm_device_mac)
                macAddress.text = deviceMacAddress
            }

            ("Video") -> {
                setContentView(R.layout.info_video_device)
                val topAppBar: MaterialToolbar = findViewById(R.id.info_video_devcice_topAppBar)
                topAppBar.setNavigationOnClickListener() {
                    finish()
                }

                val type = findViewById<TextView>(R.id.fill_info_video_devcice_type)
                type.text = "Vídeo"

                val deviceSerial = extras.getString("Serial")
                val serial = findViewById<TextView>(R.id.fill_info_video_device_serial_number)
                serial.text = deviceSerial

                val deviceUsername = extras.getString("Username")
                val username = findViewById<TextView>(R.id.fill_info_video_device_user)
                username.text = deviceUsername

            }
        }
    }
}


