package com.example.infinitestock.ui.stock

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

        // Get a reference to the AutoCompleteTextView in the layout
        val textView = view.findViewById(R.id.multiAutoCompleteTextView) as AutoCompleteTextView
        // Get the string array
        val metrics: Array<out String> = resources.getStringArray(R.array.metrics_array)
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, metrics).also { adapter ->
            textView.setAdapter(adapter)
        }
    }
}