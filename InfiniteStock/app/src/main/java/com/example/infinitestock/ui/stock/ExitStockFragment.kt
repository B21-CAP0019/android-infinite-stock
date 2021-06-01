package com.example.infinitestock.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.infinitestock.R

class ExitStockFragment : Fragment() {

    companion object {
        fun newInstance() = ExitStockFragment()
    }

    private lateinit var viewModel: ExitStockViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exit_stock, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExitStockViewModel::class.java)
        // TODO: Use the ViewModel
    }

}