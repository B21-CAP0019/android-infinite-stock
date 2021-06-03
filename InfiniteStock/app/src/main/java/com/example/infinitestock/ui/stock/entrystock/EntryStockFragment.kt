package com.example.infinitestock.ui.stock.entrystock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinitestock.ListItemAdapter
import com.example.infinitestock.R
import com.example.infinitestock.databinding.FragmentEntryStockBinding
import com.example.infinitestock.data.entity.StockItem
import com.example.infinitestock.ui.stock.input.EntryInputFragment

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
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerList(viewModel.getAllItems())

        val mFragmentManager = fragmentManager
        val mHomeFragment = EntryInputFragment()
        val mfragment = mFragmentManager?.findFragmentByTag(EntryInputFragment::class.java.simpleName)
        binding.fabAdd.setOnClickListener {
            if (mfragment !is EntryInputFragment) {
                viewModel.tempString = ""
                if (mFragmentManager != null) {
                    mFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_stock_activity, mHomeFragment, EntryInputFragment::class.java.simpleName)
                        .commit()
                }
            }
        }
    }

    private fun showRecyclerList(listItem: ArrayList<StockItem>) {
        binding.entryList.layoutManager = LinearLayoutManager(activity)
        val listItemAdapter = ListItemAdapter(listItem)
        binding.entryList.adapter = listItemAdapter

        listItemAdapter.setOnItemClickCallback(object: ListItemAdapter.OnItemClickCallback{
            override fun onItemClicked(data: StockItem) {
                val nFragmentManager = fragmentManager
                val nHomeFragment = EntryInputFragment()
                val nfragment = nFragmentManager?.findFragmentByTag(EntryInputFragment::class.java.simpleName)
                binding.fabAdd.setOnClickListener {
                    if (nfragment !is EntryInputFragment) {
                        viewModel.tempString = data.name
                        if (nFragmentManager != null) {
                            nFragmentManager
                                .beginTransaction()
                                .replace(R.id.main_stock_activity, nHomeFragment, EntryInputFragment::class.java.simpleName)
                                .commit()
                        }
                    }
                }
            }
        })
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EntryStockViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

}