package com.adhan.cache

import androidx.room.*

@Dao
interface PrayerDao {
    @Query("SELECT * FROM Prayer")
    suspend fun getAll(): List<Prayer>

    @Query("SELECT time FROM Prayer WHERE name LIKE :name  LIMIT 1")
    suspend fun findByName(name: String): String

    @Query("UPDATE Prayer SET time = :time WHERE designation LIKE :designation")
    suspend fun updateTime(designation: String,time:String)
    @Insert
    suspend fun insert(prayer: Prayer)

    @Delete
    suspend fun delete(prayer: Prayer)
}