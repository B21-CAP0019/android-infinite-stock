package com.example.infinitestock

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitestock.databinding.ItemRowBinding
import com.example.infinitestock.data.entity.StockItem

class ListItemAdapter(private val listItem: ArrayList<StockItem>): RecyclerView.Adapter<ListItemAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class ListViewHolder(val binding: ItemRowBinding)
        : RecyclerView.ViewHolder(binding.root)

    var listItems = ArrayList<StockItem>()
        set(listItems) {
            if (listItems.size > 0) {
                this.listItems.clear()
            }
            this.listItems.addAll(listItems)
            notifyDataSetChanged()
        }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder){
            with(listItem[position]){
                binding.rvProductName.text = name
                binding.rvRemainStock.text = "Stok: $stock"
                holder.itemView.setOnClickListener{
                    onItemClickCallback.onItemClicked(listItem[position])
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StockItem)
    }
}