package com.example.exam.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exam.R
import com.example.exam.activity.MainActivity
import com.example.exam.databinding.CoinListItemBinding
import com.example.exam.db.entity.CoinRoom
import com.example.exam.fragments.CoinDetailFragment
import java.util.*
import kotlin.math.roundToInt

class CoinAdapter(private val coins: List<CoinRoom>) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    class CoinViewHolder(val binding: CoinListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CoinListItemBinding.inflate(layoutInflater, parent, false)

        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coins[position]

        holder.binding.apply {
            name = coin.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            symbol = coin.symbol
            currentPrice = (coin.currentPrice * 100).roundToInt().toDouble() / 100
            ivLiked.visibility = if (coin.favourite) View.VISIBLE else View.GONE

            Glide
                .with(root.context)
                .load(coin.image)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivImage)

            holder.binding.root.setOnClickListener {
                (it.context as MainActivity).supportFragmentManager.commit {
                    val bundle = Bundle()
                    bundle.putString("coin_name", coin.name)
                    bundle.putString("coin_id", coin.id)
                    replace(R.id.container_view, CoinDetailFragment::class.java, bundle)
                    addToBackStack("coin_list_to_coin_details")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return coins.size
    }
}