package com.example.infinitestock.ui.stock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.PredictStock
import com.example.infinitestock.databinding.RowItemTableBinding
import com.example.infinitestock.ui.update.PredictsAdapter

class HistoryAdapter(private val historyItems: ArrayList<PredictStock>): RecyclerView.Adapter<PredictsAdapter.PredictsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_table, parent, false)

        return PredictsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictsViewHolder, position: Int) {
        holder.bind(historyItems[position])
    }

    override fun getItemCount(): Int = historyItems.size

    inner class PredictsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = RowItemTableBinding.bind(itemView)

        fun bind(prediction: PredictStock) {
            with (binding) {
                valueDateTable.text = prediction.date
                valuePrediction.text = prediction.qty
            }
        }
    }
}