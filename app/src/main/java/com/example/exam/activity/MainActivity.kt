package com.example.exam.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.exam.adapter.CoinAdapter
import com.example.exam.databinding.ActivityMainBinding
import com.example.exam.db.AppDatabase
import com.example.exam.db.entity.CoinRoom
import com.example.exam.factory.CoinViewModelFactory
import com.example.exam.repository.CoinRepository
import com.example.exam.service.CoinService
import com.google.android.material.snackbar.Snackbar
import com.example.exam.util.NetworkUtil
import com.example.exam.viewmodel.CoinViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var db: RoomDatabase

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/coins/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    private lateinit var coinService: CoinService

    private lateinit var coinRepository: CoinRepository

    lateinit var coinViewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
        observeData()

        if (!NetworkUtil.isConnected(this)) {
            Snackbar.make(
                binding.root,
                "No internet connection, information could be outdated",
                Snackbar.LENGTH_LONG
            ).show()
        }

        GlobalScope.launch {
            coinViewModel.getCoins()
        }
    }

    private fun init() {

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "coins_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        val coinsDao = (db as AppDatabase).coinDao()

        coinService = retrofit.create(CoinService::class.java)
        coinRepository = CoinRepository(this, coinService, coinsDao)

        val coinViewModelFactory = CoinViewModelFactory(coinRepository)
        coinViewModel = ViewModelProvider(
            this,
            coinViewModelFactory
        )[CoinViewModel::class.java]
    }

    private fun observeData() {
        GlobalScope.launch {
            coinViewModel.coinsList.collect {
                runOnUiThread {
                    val coins = it
                    val sortedCoins =
                        coins.sortedWith(compareByDescending<CoinRoom> { it.favourite }
                            .thenByDescending { it.marketCap })
                    val adapter = CoinAdapter(sortedCoins)
                    binding.coinsList.adapter = adapter
                    binding.tvCoinsCount.text = "Number Of Coins: ${it.size}"
                }
            }
        }
    }
}