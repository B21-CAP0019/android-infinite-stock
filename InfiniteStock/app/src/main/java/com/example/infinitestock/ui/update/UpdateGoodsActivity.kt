package com.example.infinitestock.ui.update

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.data.entity.Good
import com.example.infinitestock.data.entity.PredictStock
import com.example.infinitestock.databinding.ActivityUpdateGoodsBinding
import com.example.infinitestock.ui.main.MainActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class UpdateGoodsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateGoodsBinding
    private lateinit var good: Good
    private lateinit var account: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateGoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        account = SessionCompat(this).getAccount()

        // toolbar
        setSupportActionBar(binding.appbarUpdateGoods.toolbar)
        supportActionBar?.title = " ${ resources.getString(R.string.header_update_goods) }"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.animationLoading.visibility = View.VISIBLE
        // put the last saved value
        good = intent.getParcelableExtra<Good>(EXTRA_GOOD) as Good
        binding.valueUpdateGoodsName.setText(good.goodsName)
        binding.valueUpdateGoodsPrice.setText(good.goodsPrice.toString())
        good.goodsQuantity?.let { binding.valueUpdateGoodsStock.setText(it.toString()) }
        binding.valueUpdateGoodsUnit.setText(good.goodsUnit)

        binding.btnUpdateGoods.setOnClickListener {
            updateGoodsClick()
        }
        binding.btnPredictGoods.setOnClickListener {
            predictGoodsClick(good.goodsId)
        }
        binding.btnIncreaseUpdate.setOnClickListener {
            increaseUpdateClick()
        }
        binding.btnDecreaseUpdate.setOnClickListener {
            decreaseUpdateClick()
        }

        binding.animationLoading.visibility = View.INVISIBLE
    }

    private fun decreaseUpdateClick() {
        good.goodsQuantity = good.goodsQuantity?.minus(1)
        good.goodsQuantity?.let { binding.valueUpdateGoodsStock.setText(it.toString()) }
    }

    private fun increaseUpdateClick() {
        good.goodsQuantity = good.goodsQuantity?.plus(1)
        good.goodsQuantity?.let { binding.valueUpdateGoodsStock.setText(it.toString()) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return true
    }

    private fun showRecyclerList(list: ArrayList<PredictStock>) {
        binding.recyclerviewPredict.layoutManager = LinearLayoutManager(this)
        val predictsAdapter = PredictsAdapter(list)
        binding.recyclerviewPredict.adapter = predictsAdapter
    }

    private fun predictGoodsClick(id: Int) {
        switchPredicting("loading", getString(R.string.predict_message))
        // query
        val url = resources.getString(R.string.server) + "/warehouse/goods/demand/predict/$id"
        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("public_id", account.publicId)

        client.get(url, params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                binding.animationLoading.visibility = View.INVISIBLE
                binding.headerPrediction.visibility = View.VISIBLE
                // parsing JSON
                if (responseBody != null) {
                    val result = String(responseBody)
                    val response = JSONObject(result)

                    Log.d("API", result)

                    // Parse response to WarehouseResponse
                    val status = response.getInt("status")
                    val message = response.getString("message")
                    val data = response.getJSONArray("data")
                    //val dataPerDay = data.get("day")

                    val predictsItem = ArrayList<PredictStock>()
                    if (status != 0) {
                        if (data is JSONArray) {
                            for (i in 0 until data.length()) {
                                predictsItem.add(
                                    PredictStock(
                                        date = data.getJSONObject(i).getString("date"),
                                        qty = data.getJSONObject(i).getString("prediction")
                                    )
                                )
                            }
                        }
                        // put on the recyclerView
                        switchPredicting("done")
                        binding.tablePrediction.visibility = View.VISIBLE
                        showRecyclerList(predictsItem)
                    } else {
                        switchPredicting("error", message)
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                switchPredicting("error", "[$statusCode]: ${ error?.localizedMessage.toString() }")
            }
        })
    }

    private fun updateGoodsClick() {
        binding.animationLoading.visibility = View.VISIBLE

        val goodsName = binding.valueUpdateGoodsName.text.toString()
        val goodsPrice = binding.valueUpdateGoodsPrice.text.toString()
        val goodsQuantity = binding.valueUpdateGoodsStock.text.toString()
        val goodsUnit = binding.valueUpdateGoodsUnit.text.toString()
        val goodsId = good.goodsId

        val url = resources.getString(R.string.server) + "/warehouse/goods/update"
        val client = AsyncHttpClient()

        val params = RequestParams()

        params.put("public_id", account.publicId)
        params.put("goods_name", goodsName)
        params.put("goods_id", goodsId)
        params.put("goods_quantity", goodsQuantity)
        params.put("goods_unit", goodsUnit)
        params.put("goods_price", goodsPrice)

        client.put(url, params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                binding.animationLoading.visibility = View.INVISIBLE
                val intentToMainActivity = Intent(this@UpdateGoodsActivity, MainActivity::class.java)
                startActivity(intentToMainActivity)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.animationLoading.visibility = View.INVISIBLE
                Toast.makeText(this@UpdateGoodsActivity, "ErrorCode: $statusCode", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun switchPredicting(status: String, message: String = "") {
        when (status) {
            "loading" -> {
                with(binding.customLoading) {
                    root.visibility = View.VISIBLE
                    animationLoad.visibility = View.VISIBLE
                    animationEmpty.visibility = View.GONE
                    animationError.visibility = View.GONE

                    animationLoad.progress = 0.0F
                    animationLoad.playAnimation()

                    tvStatus.text = message
                }
            }
            "error" -> {
                with(binding.customLoading) {
                    root.visibility = View.VISIBLE
                    animationLoad.visibility = View.GONE
                    animationEmpty.visibility = View.GONE
                    animationError.visibility = View.VISIBLE

                    animationError.progress = 0.0F
                    animationError.playAnimation()

                    tvStatus.text = message
                }
            }
            "done" -> {
                with(binding.customLoading) {
                    root.visibility = View.GONE
                    animationLoad.visibility = View.GONE
                    animationEmpty.visibility = View.GONE
                    animationError.visibility = View.GONE

                    tvStatus.text = message
                }
            }
        }
    }

    companion object{
        const val EXTRA_GOOD = "extra_good"
    }
}