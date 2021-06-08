package com.example.infinitestock.ui.stock.exitstock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinitestock.databinding.FragmentExitStockBinding
import com.example.infinitestock.ui.stock.HistoryAdapter

class ExitStockFragment : Fragment() {

    private var _binding: FragmentExitStockBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ExitStockViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExitStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding) {
            historyAdapter = HistoryAdapter()
            exitList.layoutManager = LinearLayoutManager(requireContext())
            exitList.adapter = historyAdapter
            exitList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            viewModel = ViewModelProvider(requireActivity())[
                    ExitStockViewModel::class.java
            ]
            // TODO: Use the ViewModel
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}