package com.example.challenge_squad_apps.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.challenge_squad_apps.webclient.models.Favorites

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFavoriteDevice(favorite: Favorites)

    @Query("SELECT * FROM Favorites")
    fun getFavoriteDeviceList(): MutableList<Favorites>

    @Query("SELECT * FROM Favorites WHERE id = :id")
    fun getFavoriteDeviceByID(id: String): Favorites

    @Query("SELECT EXISTS (SELECT 1 FROM Favorites WHERE id = :id)")
    fun haveFavoriteDevice(id: String): Boolean

    @Query("DELETE FROM Favorites WHERE id = :id")
    fun deleteFavoriteDevice(id: String): Int
}