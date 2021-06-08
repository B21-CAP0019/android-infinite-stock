package com.example.infinitestock.ui.stock.entrystock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinitestock.data.entity.HistoryResponse
import com.example.infinitestock.databinding.FragmentEntryStockBinding
import com.example.infinitestock.ui.stock.HistoryAdapter
import com.example.infinitestock.ui.stock.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EntryStockFragment : Fragment() {

    private var _binding: FragmentEntryStockBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding) {
            historyAdapter = HistoryAdapter()
            entryList.layoutManager = LinearLayoutManager(requireContext())
            entryList.adapter = historyAdapter
            entryList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            fabRefresh.visibility = View.GONE

            viewModel = ViewModelProvider(requireActivity())[
                    HistoryViewModel::class.java
            ]
            viewModel.getHistoryResponse().observe(viewLifecycleOwner, { applyReportResponse(it)})
            viewModel.retrieveResponses(requireContext(), async = true, isEntryStock = true)

            fabRefresh.setOnClickListener {
                refreshResponse()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refreshResponse() {
        binding.fabRefresh.visibility = View.GONE
        with (binding.customLoading) {
            root.visibility = View.VISIBLE
            animationLoad.visibility = View.VISIBLE
            animationLoad.progress = 0.0F
            animationLoad.playAnimation()

            // empty
            animationEmpty.visibility = View.GONE
            animationEmpty.progress = 0.0F
            //animationEmpty.playAnimation()
            animationEmpty.pauseAnimation()

            // error
            animationError.visibility = View.GONE
            animationError.progress = 0.0F
            //animationError.playAnimation()
            animationError.pauseAnimation()

            tvStatus.text = getString(com.example.infinitestock.R.string.loading)
            tvStatus.visibility = View.VISIBLE
        }
        binding.entryList.visibility = View.GONE

        GlobalScope.launch(Dispatchers.Main) {
            val deferredReports = async(Dispatchers.IO) {
                viewModel.retrieveResponses(requireContext(), async = false, isEntryStock = false)
            }
            val reportResponse = deferredReports.await()
            applyReportResponse(reportResponse)
        }
    }

    private fun applyReportResponse(response: HistoryResponse) {
        binding.fabRefresh.visibility = View.VISIBLE
        if (response.status == 200) {
            binding.customLoading.animationLoad.visibility = View.GONE

            if (response.totalData > 0) {
                with (binding.customLoading) {
                    // empty
                    animationEmpty.visibility = View.GONE
                    animationEmpty.progress = 0.0F
                    //animationEmpty.playAnimation()
                    animationEmpty.pauseAnimation()

                    // error
                    animationError.visibility = View.GONE
                    animationError.progress = 0.0F
                    //animationError.playAnimation()
                    animationError.pauseAnimation()

                    tvStatus.visibility = View.GONE
                    root.visibility = View.GONE
                }
                binding.entryList.visibility = View.VISIBLE
                historyAdapter.setItems(response.reportItems)
            } else {
                // When the data is empty
                with (binding.customLoading) {
                    // empty
                    animationEmpty.visibility = View.VISIBLE
                    animationEmpty.progress = 0.0F
                    animationEmpty.playAnimation()
                    //animationEmpty.pauseAnimation()

                    // error
                    animationError.visibility = View.GONE
                    animationError.progress = 0.0F
                    //animationError.playAnimation()
                    animationError.pauseAnimation()

                    tvStatus.text = getString(com.example.infinitestock.R.string.empty_exit_stock)
                    root.visibility = View.VISIBLE
                }
                binding.entryList.visibility = View.GONE
                historyAdapter.setItems(ArrayList())
            }
        } else {
            with (binding.customLoading) {
                // empty
                animationEmpty.visibility = View.GONE
                animationEmpty.progress = 0.0F
                //animationEmpty.playAnimation()
                animationEmpty.pauseAnimation()

                // error
                animationError.visibility = View.VISIBLE
                animationError.progress = 0.0F
                animationError.playAnimation()
                //animationError.pauseAnimation()

                tvStatus.text = getString(com.example.infinitestock.R.string.error)
                root.visibility = View.VISIBLE
            }
            binding.entryList.visibility = View.GONE
            historyAdapter.setItems(ArrayList())
            Log.e("API Error", response.message!!)
        }
    }

}