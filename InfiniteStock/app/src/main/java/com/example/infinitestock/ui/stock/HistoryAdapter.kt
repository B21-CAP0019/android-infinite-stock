package com.example.infinitestock.ui.stock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.ReportItem
import com.example.infinitestock.databinding.CardHistoryBinding

class HistoryAdapter(private val historyItems: ArrayList<ReportItem>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
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
                historyQuantity.text = reportItem.qty
                historyUnit.text =reportItem.unit
            }
        }
    }
}