package com.example.infinitestock.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        Glide.with(this@MainActivity)
            .load(R.drawable.logo)
            .into(binding.appbarMain.logoMain)
        setContentView(binding.root)

    }

}