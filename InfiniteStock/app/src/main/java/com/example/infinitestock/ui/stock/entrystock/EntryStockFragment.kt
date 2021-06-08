package com.example.infinitestock.ui.stock.entrystock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinitestock.data.entity.ReportItem
import com.example.infinitestock.databinding.FragmentEntryStockBinding
import com.example.infinitestock.ui.stock.HistoryAdapter

class EntryStockFragment : Fragment() {

    companion object {
        fun newInstance() = EntryStockFragment()
    }

    private lateinit var viewModel: EntryStockViewModel
    private var _binding: FragmentEntryStockBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EntryStockViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.animationLoading.visibility = View.VISIBLE
        //showRecyclerList(requireActivity().let { viewModel.retrieveEntryReport(it.baseContext, async = true) })
    }

    private fun showRecyclerList(list: ArrayList<ReportItem>) {
        binding.entryList.layoutManager = LinearLayoutManager(activity?.baseContext)
        val historyAdapter = HistoryAdapter()
        binding.entryList.adapter = historyAdapter

        binding.animationLoading.visibility = View.INVISIBLE
    }
}