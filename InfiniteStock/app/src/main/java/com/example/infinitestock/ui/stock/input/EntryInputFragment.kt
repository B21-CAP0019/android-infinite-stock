package com.example.infinitestock.ui.stock.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.infinitestock.R
import com.example.infinitestock.databinding.FragmentEntryInputBinding
import com.example.infinitestock.entity.StockItem
import com.example.infinitestock.ui.stock.entrystock.EntryStockViewModel

class EntryInputFragment : Fragment() {

    lateinit var viewModel: EntryStockViewModel
    private var _binding: FragmentEntryInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EntryStockViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryInputBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set autocomplete bagian satuan
        // Get a reference to the AutoCompleteTextView in the layout
        val textView = view.findViewById(R.id.input_metric) as AutoCompleteTextView
        // Get the string array
        val countries: Array<out String> = resources.getStringArray(R.array.metrics_array)
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, countries).also { adapter ->
            textView.setAdapter(adapter)
        }

        // Menampilkan data jika aksi yang ingin dilakukan adalah edit
        lateinit var item: StockItem
        if (viewModel.tempString != "") {
            item = viewModel.getItem(viewModel.tempString)

            binding.button.text = "EDIT"
            binding.inputName.setText(item.name)
            binding.inputQty.setText(item.stock)
            //binding.inputMetric.setText(???)
        }

        // Menerima objek yang diinputkan
        binding.button.setOnClickListener {
            val mItem= StockItem()
            mItem.name = binding.inputName.text.toString().trim()
            mItem.stock =binding.inputQty.text.toString().trim().toInt()

            viewModel.addItem(mItem)
        }
    }
}