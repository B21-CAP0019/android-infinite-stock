package com.example.infinitestock.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.databinding.ActivityMainBinding
import com.example.infinitestock.ui.add.AddGoodsActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ACCOUNT = "account"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var account: Account? = null
    private lateinit var goodsAdapter : GoodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goodsAdapter = GoodsAdapter()

        val bundle = intent.extras
        account = bundle?.getParcelable(EXTRA_ACCOUNT)

        with (binding) {
            Glide.with(this@MainActivity)
                .load(R.drawable.logo)
                .into(appbarMain.logoMain)

            getGoodsList()

            btnToAddGoods.setOnClickListener {
                val intentToAddGoodsActivity = Intent(this@MainActivity, AddGoodsActivity::class.java)
                startActivity(intentToAddGoodsActivity)
            }
        }
    }

    private fun getGoodsList() {
        val url = resources.getString(R.string.server) + "/warehouse/get/goods/all"
        val client = AsyncHttpClient()

        client.addHeader("x-access-publicid", account?.publicId)
    }

}