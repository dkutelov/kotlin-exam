package com.example.exam.db.dao

import androidx.room.*
import com.example.exam.db.entity.CoinRoom

@Dao
interface CoinDao {

    @Query("SELECT * FROM coins")
    suspend fun getCoins(): List<CoinRoom>

    @Query("SELECT * FROM coins WHERE name=:name")
    suspend fun getCoinByName(name: String): CoinRoom

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(coins: List<CoinRoom>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(coin: CoinRoom)
}