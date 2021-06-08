package com.example.infinitestock.ui.stock

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivityStockBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HistoryActivity : AppCompatActivity() {

    private var _binding: ActivityStockBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}