package com.example.exam.repository

import android.content.Context
import android.util.Log
import com.example.exam.db.dao.CoinDao
import com.example.exam.db.entity.CoinRoom
import com.example.exam.models.CoinResponse
import com.example.exam.service.CoinService
import com.example.exam.util.NetworkUtil

class CoinRepository constructor(
    private val context: Context,
    private val coinService: CoinService,
    private val coinDao: CoinDao
) {

    suspend fun getCoins(): List<CoinRoom> {
        return try {

            if (NetworkUtil.isConnected(context)) {

                val coins = coinService.getCoins().execute().body()

                val roomCoins = coins?.map { mapResponseToDbModel(it) }
                coinDao.insertAll(roomCoins ?: return arrayListOf())
            }

            coinDao.getCoins()
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    suspend fun getCoinByName(name: String): CoinRoom? {
        return try {
            return coinDao.getCoinByName(name)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateCoin(coin: CoinRoom) {
        coinDao.update(coin)
    }

    private fun mapResponseToDbModel(response: CoinResponse): CoinRoom {
        return CoinRoom(
            id = response.id ?: "",
            name = response.name ?: "",
            image = response.image ?: "",
            symbol = response.symbol ?: "",
            currentPrice = response.current_price ?: 0.0,
            marketCap = response.market_cap ?: 0.0,
            high24h = response.high_24h ?: 0.0,
            low24h = response.low_24h ?: 0.0,
            priceChangePercentage24h = response.price_change_percentage_24h ?: 0.0,
            marketCapChangePercentage24h = response.market_cap_change_percentage_24h ?: 0.0,
            favourite = false
        )
    }
}