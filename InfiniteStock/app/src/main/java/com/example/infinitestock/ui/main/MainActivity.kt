package com.example.infinitestock.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.databinding.ActivityMainBinding
import com.example.infinitestock.ui.add.AddGoodsActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ACCOUNT = "account"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var account: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        account = bundle?.getParcelable(EXTRA_ACCOUNT)

        Glide.with(this@MainActivity)
            .load(R.drawable.logo)
            .into(binding.appbarMain.logoMain)

        binding.btnToAddGoods.setOnClickListener {
            val intentToAddGoodsActivity = Intent(this@MainActivity, AddGoodsActivity::class.java)
            startActivity(intentToAddGoodsActivity)
        }

    }

}