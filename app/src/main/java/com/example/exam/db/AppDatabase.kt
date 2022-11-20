package com.example.exam.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exam.db.dao.CoinDao
import com.example.exam.db.entity.CoinRoom

@Database(entities = [CoinRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}