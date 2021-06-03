package com.example.infinitestock.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivityAddGoodsBinding

class AddGoodsActivity : AppCompatActivity() {

    private var _binding:ActivityAddGoodsBinding ? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddGoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this@AddGoodsActivity)
            .load(R.drawable.logo)
            .into(binding.appbarAddGoods.logoMain)
    }
}