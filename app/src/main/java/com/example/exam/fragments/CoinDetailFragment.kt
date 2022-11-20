package com.example.exam.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.exam.R
import com.example.exam.activity.MainActivity
import com.example.exam.databinding.FragmentCoinDetailBinding
import com.example.exam.db.entity.CoinRoom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CoinDetailFragment : Fragment() {

    private lateinit var binding: FragmentCoinDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedCoinName = arguments?.getString("coin_name", null)

        GlobalScope.launch {
            (activity as? MainActivity)?.coinViewModel?.getCoinByName(
                selectedCoinName ?: return@launch
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinDetailBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    private fun observeData() {
        GlobalScope.launch {
            (activity as? MainActivity)?.coinViewModel?.selectedCoin?.collect {

                binding.coin = it ?: return@collect

                (activity as? MainActivity)?.runOnUiThread {
                    setDataBinding(it)
                    Glide
                        .with(context ?: return@runOnUiThread)
                        .load(it.image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(binding.ivLogo)
                }
            }
        }

    }

    private fun setDataBinding(coin: CoinRoom?) {
        binding.coin ?: return

        if (binding.coin?.favourite == true) {
            binding.btnLike.setImageResource(android.R.drawable.star_big_on)
        } else {
            binding.btnLike.setImageResource(android.R.drawable.star_big_off)
        }

        val marketCapitalization = coin?.marketCap?.toInt() ?: 0
        binding.tvMarketCap.text = marketCapitalization.toString()

        val priceChange = coin?.priceChangePercentage24h as Double
        val priceChangeText = "${format2Decimals(priceChange)}%"
        binding.tvPercentPriceChange.text = priceChangeText

        if (priceChange >= 0) {
            binding.tvPercentPriceChange.setTextColor(Color.parseColor("#AAFF00"))
        }

        val capChange = coin.marketCapChangePercentage24h as Double
        val capChangeText = "${format2Decimals(capChange)}%"
        binding.tvCapChange.text = capChangeText

        if (capChange >= 0) {
            binding.tvCapChange.setTextColor(Color.parseColor("#AAFF00"))
        }


        binding.btnLike.setOnClickListener {

            coin.favourite = coin.favourite != true

            if (coin.favourite == true) {
                binding.btnLike.setImageResource(android.R.drawable.star_big_on)
            } else {
                binding.btnLike.setImageResource(android.R.drawable.star_big_off)
            }

            GlobalScope.launch {
                (activity as? MainActivity)?.coinViewModel?.updateFavourites(
                    coin ?: return@launch
                )
            }
        }
    }

    private fun format2Decimals(value: Double?): Double {
        val x = value ?: 0.0
        return ((x * 100).toInt() / 100).toDouble()
    }
}