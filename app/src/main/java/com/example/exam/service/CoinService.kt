package com.example.exam.service

import com.example.exam.models.CoinResponse
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path

interface CoinService {

    @GET("markets?vs_currency=usd")
    fun getCoins(): Call<List<CoinResponse>>
}