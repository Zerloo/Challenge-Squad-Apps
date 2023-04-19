package com.example.challenge_squad_apps

import com.example.challenge_squad_apps.webclient.models.AlarmDevice
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class AlarmWebClient {
    private val token: String =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjZlOWFkM2E2LWQyYTUtNDg1MC1iNjNjLTU4YjU5YzI4NWNjNCIsImlhdCI6MTY4MTkwNTMxMywiZXhwIjoxNjg0NDk3MzEzfQ.HqIloqH6XhZxb1QBtHMNZEL1FYBrHuTxnqRO8towmrk"

    suspend fun getAlarm(): List<AlarmDevice> {
        val response = retrofitInitialization(token).alarmDeviceService.alarmDeviceService()
        val alarmDevices = mutableListOf<AlarmDevice>()
        if (response.isSuccessful) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(ResponseData::class.java)
            val responseData = response.body()?.string()?.let { adapter.fromJson(it) }
            responseData?.data?.let { dataArray ->
                for (deviceJson in dataArray) {
                    val device = AlarmDevice(
                        id = deviceJson.id,
                        name = deviceJson.name,
                        type = "Alarm",
                        macAddress = deviceJson.macAddress,
                        password = deviceJson.password,
                        favorite = "false",

                        )
                    alarmDevices.add(device)
                }
            }
        }
        return alarmDevices
    }

    data class ResponseData(val count: Int, val data: List<DeviceJson>)
    data class DeviceJson(
        val id: String,
        val name: String,
        val macAddress: String,
        val password: String
    )
}
