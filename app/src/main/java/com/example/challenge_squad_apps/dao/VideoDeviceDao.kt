package com.example.challenge_squad_apps.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.challenge_squad_apps.webclient.models.VideoDevice
import kotlinx.coroutines.flow.Flow


@Dao
interface VideoDeviceDao {

    @Insert (onConflict = REPLACE)
    suspend fun saveVideoDevice (videoDevice: List<VideoDevice>)

    @Query("SELECT * FROM VideoDevice")
    fun getVideoDevice () : Flow<List<VideoDevice>>

    @Query("DELETE FROM VideoDevice WHERE id = :id")
    suspend fun deleteVideoDevice(id: String)

}