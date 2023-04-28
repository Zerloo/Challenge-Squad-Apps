package com.example.challenge_squad_apps.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.challenge_squad_apps.webclient.models.AlarmDevice
import kotlinx.coroutines.flow.Flow


@Dao
interface AlarmDeviceDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveAlarmDevice(AlarmDevice: List<AlarmDevice>)

    @Query("SELECT * FROM AlarmDevice")
    fun getAlarmDevice(): Flow<List<AlarmDevice>>

    @Query("DELETE FROM AlarmDevice WHERE id = :id")
    suspend fun deleteAlarmDevice(id: String)
}