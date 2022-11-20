package com.example.exam.viewmodel

import androidx.lifecycle.ViewModel
import com.example.exam.db.entity.CoinRoom
import com.example.exam.repository.CoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoinViewModel(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val coinsListStateFlow = MutableStateFlow<List<CoinRoom>>(arrayListOf())
    val coinsList: StateFlow<List<CoinRoom>> = coinsListStateFlow.asStateFlow()

    private val selectedCoinStateFlow = MutableStateFlow<CoinRoom?>(null)
    val selectedCoin: StateFlow<CoinRoom?> = selectedCoinStateFlow.asStateFlow()

    suspend fun getCoins() {
        val coins = coinRepository.getCoins()
        coinsListStateFlow.value = coins.sortedByDescending { it.marketCap }
    }

    suspend fun getCoinByName(name: String) {
        val coin = coinRepository.getCoinByName(name)
        selectedCoinStateFlow.value = coin ?: return
    }

    suspend fun updateFavourites(coin: CoinRoom) {
        coinRepository.updateCoin(coin)
        selectedCoinStateFlow.value =
            selectedCoinStateFlow.value?.copy(favourite = coin.favourite)
    }
}