package com.example.infinitestock.ui.stock.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.ReportItem
import com.example.infinitestock.databinding.CardHistoryBinding
import kotlin.math.abs
import kotlin.math.roundToInt

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val historyItems = ArrayList<ReportItem>()

    fun setItems(historyItems: ArrayList<ReportItem>) {
        this.historyItems.clear()
        this.historyItems.addAll(historyItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_history, parent, false)

        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyItems[position])
    }

    override fun getItemCount(): Int = historyItems.size

    inner class HistoryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = CardHistoryBinding.bind(itemView)

        fun bind(reportItem: ReportItem) {
            with (binding) {
                historyDate.text = reportItem.dateTime
                historyName.text = reportItem.name
                historyQuantity.text = formatDecimal(reportItem.qty)
                historyUnit.text = reportItem.unit
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
}