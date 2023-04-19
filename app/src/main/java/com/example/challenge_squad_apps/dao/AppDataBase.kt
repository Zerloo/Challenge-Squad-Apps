package com.example.challenge_squad_apps

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [VideoDevice::class, AlarmDevice::class]
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun videoDeviceDao() : VideoDeviceDao
    abstract fun alarmDeviceDao() : AlarmDeviceDao

    companion object {
        @Volatile
        private var db: AppDataBase? = null

        fun instancia(context: Context): AppDataBase {
            return db ?: Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "challenge.db"
            ).build()
        }
    }
}