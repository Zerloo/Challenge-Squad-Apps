package com.example.challenge_squad_apps.ui.activities.infoDevice

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge_squad_apps.databinding.InfoDeviceBinding
import com.example.challenge_squad_apps.ui.utils.enums.DeviceType

class InfoDeviceActivity : AppCompatActivity() {


    private lateinit var binding: InfoDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = InfoDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val deviceType = extras?.getString("Type")

        setupView(deviceType, extras)
        setupListeners()
    }

    private fun setupListeners() {
        binding.infoDeviceTopAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupView(deviceType: String?, extras: Bundle?) {
        with(binding) {
            if (deviceType == DeviceType.ALARM.type){
                fillInfoDevciceType.text = DeviceType.ALARME.type
                fillInfoDeviceMac.text = extras!!.getString("Mac Address")
                hideVideoInfo()
            }
            else if (deviceType == DeviceType.VIDEO.type){
                fillInfoDevciceType.text = DeviceType.VÍDEO.type
                fillInfoDeviceSerialNumber.text = extras!!.getString("Serial")
                fillInfoDeviceUser.text = extras.getString("Username")
                hideAlarmInfo()
            }
        }
    }

    private fun hideAlarmInfo(){
            binding.infoLinearLayoutMac.visibility = View.GONE
    }
    private fun hideVideoInfo(){
        with(binding){
            infoLinearLayoutUser.visibility = View.GONE
            infoLinearLayoutSerialNumber.visibility = View.GONE
        }
    }
}


