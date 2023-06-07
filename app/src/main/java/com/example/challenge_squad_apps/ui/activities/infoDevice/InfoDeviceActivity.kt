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
        val deviceType = extras?.getString("type")

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
                fillInfoDeviceMac.text = (extras!!.getString("rastreability"))
                hideVideoInfo()
            }
            else if (deviceType == DeviceType.VIDEO.type){
                fillInfoDevciceType.text = DeviceType.VÍDEO.type
                fillInfoDeviceSerialNumber.text = extras!!.getString("rastreability")
                fillInfoDeviceUser.text = extras.getString("username")
                hideAlarmInfo()
            }
        }
    }

//    private fun formatMacAddress(macAddress: String?): String {
//        val formattedMacAddress = StringBuilder()
//
//        val sanitizedMacAddress = macAddress?.replace(":", "")!!.replace("-", "")
//
//        for (i in sanitizedMacAddress.indices step 2) {
//            formattedMacAddress.append(sanitizedMacAddress.substring(i, i + 2))
//            if (i < sanitizedMacAddress.length - 2) {
//                formattedMacAddress.append(":")
//            }
//        }
//
//        return formattedMacAddress.toString()
//    }


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


