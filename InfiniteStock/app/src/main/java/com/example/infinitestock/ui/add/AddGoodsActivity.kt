package com.example.infinitestock.ui.add

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.databinding.ActivityAddGoodsBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class AddGoodsActivity : AppCompatActivity() {

    private var _binding:ActivityAddGoodsBinding ? = null
    private val binding get() = _binding!!
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddGoodsBinding.inflate(layoutInflater)

        with (binding) {
            setContentView(root)

            // toolbar
            setSupportActionBar(appbarAddGoods.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = resources.getString(R.string.header_add_goods)

            btnToOkayAddGoods.setOnClickListener {
                onAddNewItemPerformed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return true
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun onAddNewItemPerformed() {
        switchLoading()

        with(binding) {
            val goodsName = valueGoodsName.text.toString()
            val goodsPrice = valueGoodsPrice.text.toString()
            val goodsQuantity = valueGoodsQuantity.text.toString()
            val goodsUnit = valueGoodsUnit.text.toString()

            if (goodsName == "" || goodsPrice == "" || goodsQuantity == "" || goodsUnit == "") {
                Toast.makeText(
                    this@AddGoodsActivity,
                    "You must fill all the goods form above!",
                    Toast.LENGTH_SHORT
                ).show()
                switchLoading()
            } else {
                val url = resources.getString(R.string.server) + "/warehouse/goods/create"
                val client = AsyncHttpClient()

                val account = SessionCompat(this@AddGoodsActivity).getAccount()
                val params = RequestParams()
                params.put("public_id", account.publicId)
                params.put("goods_name", goodsName)
                params.put("goods_price", goodsPrice)
                params.put("goods_quantity", goodsQuantity)
                params.put("goods_unit", goodsUnit)

                client.post(url, params, object: AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray
                    ) {
                        // success
                        if (statusCode == 201) {
                            val result = String(responseBody)
                            val response = JSONObject(result)

                            val status = response.getInt("status")

                            if (status == 1) {
                                Toast.makeText(
                                    this@AddGoodsActivity,
                                    "Successfully added new item: $goodsName!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        }
                        switchLoading()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray,
                        error: Throwable?
                    ) {
                        //failure
                        switchLoading()

                        val result = String(responseBody)
                        Log.d("API Error", result)
                        val response = JSONObject(result)

                        Toast.makeText(
                            this@AddGoodsActivity,
                            "[${ statusCode }]: ${ response.getString("message") } (${ error?.message.toString() })",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    private fun switchLoading() {
        with (binding) {
            if (!isLoading) {
                btnToOkayAddGoods.visibility = View.GONE
                valueGoodsName.visibility = View.GONE
                containerQtyUnit.visibility = View.GONE
                containerPrice.visibility = View.GONE
                loading.visibility = View.VISIBLE
            } else {
                btnToOkayAddGoods.visibility = View.VISIBLE
                valueGoodsName.visibility = View.VISIBLE
                containerQtyUnit.visibility = View.VISIBLE
                containerPrice.visibility = View.VISIBLE
                loading.visibility = View.GONE
            }
            isLoading = !isLoading
        }
    }
}
