package com.example.infinitestock.ui.update

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.PredictStock
import com.example.infinitestock.databinding.RowItemTableBinding

class PredictsAdapter(private val predictions: ArrayList<PredictStock>): RecyclerView.Adapter<PredictsAdapter.PredictsViewHolder>() {
    //private val predictions = ArrayList<PredictStock>()

    //fun setGoodsValue(goods: ArrayList<PredictStock>) {
    //    this.predictions.clear()
    //    this.predictions.addAll(goods)
    //    notifyDataSetChanged()
    //}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_table, parent, false)

        return PredictsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictsViewHolder, position: Int) {
        holder.bind(predictions[position])
    }

    override fun getItemCount(): Int = predictions.size

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
