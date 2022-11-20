package com.example.exam.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinRoom(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "symbol") var symbol: String,
    @ColumnInfo(name = "current_price") var currentPrice: Double,
    @ColumnInfo(name = "market_cap") var marketCap: Double,
    @ColumnInfo(name = "high_24h") var high24h: Double,
    @ColumnInfo(name = "low_24h") var low24h: Double,
    @ColumnInfo(name = "price_change_percentage_24h") var priceChangePercentage24h: Double,
    @ColumnInfo(name = "market_cap_change_percentage_24h") var marketCapChangePercentage24h: Double,
    @ColumnInfo(name = "favourite") var favourite: Boolean
)
