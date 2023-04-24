package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge_squad_apps.R
import com.google.android.material.appbar.MaterialToolbar

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        val deviceType = extras?.getString("Type")
        when (deviceType) {
            ("Alarm") -> {
                setContentView(R.layout.edit_alarm_device)
                val topAppBar: MaterialToolbar = findViewById(R.id.edit_alarm_device_topAppBar)
                topAppBar.setNavigationOnClickListener() {
                    finish()
                }
            }

            ("Video") -> {
                setContentView(R.layout.edit_video_device)
                val topAppBar: MaterialToolbar = findViewById(R.id.edit_video_device_topAppBar)
                topAppBar.setNavigationOnClickListener() {
                    finish()
                }
            }
        }
    }
}
