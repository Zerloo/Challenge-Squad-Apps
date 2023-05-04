package com.example.challenge_squad_apps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challenge_squad_apps.dao.FavoritesDao
import com.example.challenge_squad_apps.webclient.models.Favorites

@Database(
    version = 1,
    entities = [Favorites::class]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoritesDeviceDao(): FavoritesDao

    companion object {
        @Volatile
        private var db: AppDataBase? = null

        fun instancia(context: Context): AppDataBase {
            return db ?: Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "challenge.db"
            ).allowMainThreadQueries()
                .build()
        }
    }
}
