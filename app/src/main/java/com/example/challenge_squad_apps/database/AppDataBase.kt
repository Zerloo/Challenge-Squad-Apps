package com.example.challenge_squad_apps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challenge_squad_apps.database.dao.FavoritesDao
import com.example.challenge_squad_apps.webclient.dto.models.Favorites

@Database(
    entities = [Favorites::class],
    version = 1
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun favoritesDeviceDao(): FavoritesDao
    companion object {
        @Volatile
        private var dataBase: AppDataBase? = null
        fun instance(context: Context): AppDataBase {
            return dataBase ?: Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "challenge-db"
            ).allowMainThreadQueries()
                .build()

        }
    }
}
