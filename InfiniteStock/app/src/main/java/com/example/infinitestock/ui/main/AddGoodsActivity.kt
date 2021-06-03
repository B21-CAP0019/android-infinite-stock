package com.example.infinitestock.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivityAddGoodsBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header

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

        binding!!.btnToOkayAddGoods.setOnClickListener {
            createGoodsClick()
        }
    }

    private fun createGoodsClick() {
        val goods_name = binding?.valueGoodsName?.text.toString()
        val goods_price = binding?.valueGoodsPrice?.text.toString()
        val goods_quantity = binding?.valueGoodsQuantity?.text.toString()
        val goods_unit = binding?.valueGoodsUnit?.text.toString()

        val url = resources.getString(R.string.server) + "/warehouse/create/goods"
        val client = AsyncHttpClient()

        val params = RequestParams()
        params.put("name", goods_name)
        params.put("price", goods_price)
        params.put("quantity", goods_quantity)
        params.put("unit", goods_unit)

        client.post(url, params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                // success
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                // failure
            }

        })
    }
}
