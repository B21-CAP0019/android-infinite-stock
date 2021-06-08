package com.example.infinitestock.ui.stock

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.infinitestock.ui.stock.entrystock.EntryStockFragment
import com.example.infinitestock.ui.stock.exitstock.ExitStockFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = EntryStockFragment()
            1 -> fragment = ExitStockFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}