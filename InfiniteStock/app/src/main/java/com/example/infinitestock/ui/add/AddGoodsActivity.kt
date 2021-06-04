package com.example.infinitestock.ui.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        with (binding) {
            setContentView(root)

            // toolbar
            setSupportActionBar(appbarAddGoods.toolbar)
            supportActionBar?.title = resources.getString(R.string.app_name)

            btnToOkayAddGoods.setOnClickListener {
                createGoodsClick()
            }
        }
    }

    private fun createGoodsClick() {
        val goodsName = binding.valueGoodsName.text.toString()
        val goodsPrice = binding.valueGoodsPrice.text.toString()
        val goodsQuantity = binding.valueGoodsQuantity.text.toString()
        val goodsUnit = binding.valueGoodsUnit.text.toString()

        val url = resources.getString(R.string.server) + "/warehouse/create/goods"
        val client = AsyncHttpClient()

        val params = RequestParams()
        params.put("name", goodsName)
        params.put("price", goodsPrice)
        params.put("quantity", goodsQuantity)
        params.put("unit", goodsUnit)

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
