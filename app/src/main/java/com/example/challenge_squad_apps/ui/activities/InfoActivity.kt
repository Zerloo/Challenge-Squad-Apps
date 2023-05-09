package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.databinding.InfoAlarmDeviceBinding
import com.example.challenge_squad_apps.databinding.InfoVideoDeviceBinding
import com.example.challenge_squad_apps.databinding.MainActivityBinding
import com.google.android.material.appbar.MaterialToolbar

class InfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        val deviceType = extras?.getString("Type")

        when (deviceType) {
            ("Alarm") -> {
                val binding = InfoAlarmDeviceBinding.inflate(layoutInflater)
                setContentView(binding.root)

                binding.fillInfoAlarmDeviceType.text = "Alarme"
                binding.fillInfoAlarmDeviceMac.text = extras.getString("Mac Address")
                binding.infoAlarmDeviceTopAppBar.setNavigationOnClickListener() {
                    finish()
                }
            }

            ("Video") -> {
                val binding = InfoVideoDeviceBinding.inflate(layoutInflater)
                setContentView(binding.root)

                binding.fillInfoVideoDevciceType.text = "Vídeo"
                binding.fillInfoVideoDeviceSerialNumber.text = extras.getString("Serial")
                binding.fillInfoVideoDeviceUser.text = extras.getString("Username")
                binding.infoVideoDevciceTopAppBar.setNavigationOnClickListener() {
                    finish()
                }
            }

        }
    }
}


