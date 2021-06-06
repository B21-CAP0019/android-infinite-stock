package com.example.infinitestock.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.Good
import com.example.infinitestock.databinding.CardGoodsBinding

class GoodsAdapter : RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {

    private val goods = ArrayList<Good>()

    fun setGoodsValue(goods: ArrayList<Good>) {
        this.goods.clear()
        this.goods.addAll(goods)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_goods, parent, false)

        return GoodsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.bind(goods[position])
    }

    override fun getItemCount(): Int = goods.size

    inner class GoodsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = CardGoodsBinding.bind(itemView)

        fun bind(good: Good) {
            with (binding) {
                goodsName.text = good.goodsName
                goodsPrice.text = good.goodsPrice.toString()
                goodsQuantity.text = good.goodsQuantity.toString()
                goodsUnit.text = good.goodsUnit

                root.setOnClickListener {
                    // TODO: action after user clicked the items! Directly to update items?
                }

                root.setOnLongClickListener {
                    // TODO: action after the user holds the item! Maybe delete the item?
                    return@setOnLongClickListener true // don't delete this line.
                }
            }
        }
    }
}