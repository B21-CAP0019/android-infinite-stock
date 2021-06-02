package com.example.infinitestock

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.infinitestock.databinding.ActivityDashboardBinding
import com.example.infinitestock.ui.stock.StockActivity

class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stockBtn.setOnClickListener {
            val intent = Intent(this@DashboardActivity, StockActivity::class.java)
            startActivity(intent)
        }
    }
}