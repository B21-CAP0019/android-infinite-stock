package com.example.infinitestock.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.Good
import com.example.infinitestock.databinding.CardGoodsBinding
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt

class GoodsAdapter : RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {

    private val goods = ArrayList<Good>()
    private var itemActionListener: OnItemActionListener? = null

    fun setOnItemActionListener(listener: OnItemActionListener) {
        this.itemActionListener = listener
    }

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

                val numberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                goodsPrice.text = numberFormat.format(good.goodsPrice).trim()

                goodsQuantity.text = good.goodsQuantity?.let { formatDecimal(it) }
                goodsUnit.text = good.goodsUnit

                root.setOnClickListener {
                    itemActionListener?.onItemClick(good)
                }

                root.setOnLongClickListener {
                    // TODO: action after the user holds the item! Maybe delete the item?
                    itemActionListener?.onItemLongClick(good)

                    return@setOnLongClickListener true // don't delete this line.
                }
            }
        }

        private fun formatDecimal(number: Double): String {
            val epsilon = 0.004f
            return if (abs(number.roundToInt() - number) < epsilon) {
                String.format("%.0f", number)
            } else {
                String.format("%.2f", number)
            }
        }
    }

    interface OnItemActionListener {
        fun onItemClick(good: Good)
        fun onItemLongClick(good: Good)
    }
}