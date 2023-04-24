package com.example.challenge_squad_apps.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge_squad_apps.R
import com.google.android.material.appbar.MaterialToolbar

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_device)
        val topAppBar: MaterialToolbar = findViewById(R.id.add_device_topAppBar)
        topAppBar.setNavigationOnClickListener() {
            finish()
        }
    }
}
