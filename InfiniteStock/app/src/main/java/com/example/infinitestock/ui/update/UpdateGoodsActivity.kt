package com.example.infinitestock.ui.update

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.Good
import com.example.infinitestock.databinding.ActivityUpdateGoodsBinding
import com.example.infinitestock.ui.main.MainActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header

class UpdateGoodsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateGoodsBinding
    private lateinit var good: Good

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateGoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar
        setSupportActionBar(binding.appbarUpdateGoods.toolbar)
        supportActionBar?.title = " ${ resources.getString(R.string.app_name) }"
        supportActionBar?.setDisplayUseLogoEnabled(true)
        Glide.with(this@UpdateGoodsActivity)
            .asDrawable()
            .load(R.drawable.logo)
            .override(80, 80)
            .into(object: CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    supportActionBar?.setLogo(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    /* no-op */
                }
            })

        binding.progressBar.visibility = View.VISIBLE
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
            predictGoodsClick()
        }

        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun predictGoodsClick() {
        TODO("Not yet implemented")
    }

    private fun updateGoodsClick() {
        binding.progressBar.visibility = View.VISIBLE

        val goodsName = binding.valueUpdateGoodsName.text.toString()
        val goodsPrice = binding.valueUpdateGoodsPrice.text.toString()
        val goodsQuantity = binding.valueUpdateGoodsStock.text.toString()
        val goodsUnit = binding.valueUpdateGoodsUnit.text.toString()
        val goodsId = good.goodsId

        val url = resources.getString(R.string.server) + "/warehouse/goods/update"
        val client = AsyncHttpClient()

        val account = SessionCompat(this).getAccount()

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
                binding.progressBar.visibility = View.INVISIBLE
                val intentToMainActivity = Intent(this@UpdateGoodsActivity, MainActivity::class.java)
                startActivity(intentToMainActivity)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@UpdateGoodsActivity, "ErrorCode: $statusCode", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object{
        val EXTRA_GOOD = "ekstra_good"
    }
}