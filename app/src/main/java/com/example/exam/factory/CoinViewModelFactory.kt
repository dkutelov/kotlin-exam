package com.example.exam.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exam.repository.CoinRepository
import com.example.exam.viewmodel.CoinViewModel

class CoinViewModelFactory(
    private val coinRepository: CoinRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoinViewModel(coinRepository) as T
    }
}