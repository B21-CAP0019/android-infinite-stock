package com.example.infinitestock.ui.main

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.Good
import com.example.infinitestock.data.entity.WarehouseResponse
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class GoodsViewModel: ViewModel() {

    private val warehouse = MutableLiveData<WarehouseResponse>()

    fun retrieveGoods(context: Context, async: Boolean = true) : WarehouseResponse {
        try {
            Looper.prepare()
        } catch (ignored: Exception) {
        }
        val account = SessionCompat(context).getAccount()
        val url = context.resources.getString(R.string.server) + "/warehouse/goods/get/all"
        val client = if (async) AsyncHttpClient() else SyncHttpClient()

        var warehouseResponse = WarehouseResponse(
            status = 0,
            message = "",
            totalData = 0,
            goods = ArrayList()
        )

        val params = RequestParams()
        params.put("public_id", account.publicId)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val response = JSONObject(result)

                Log.d("API", result)

                // Parse response to WarehouseResponse
                val message = response.getString("message")
                val data = response.getJSONObject("data")
                val detailData = data.get("detail_data")
                val totalData = data.getInt("total_data")

                if (detailData is JSONArray) {
                    val goods = ArrayList<Good>()
                    for (i in 0 until detailData.length()) {
                        goods.add(Good(
                            goodsId = detailData.getJSONObject(i).getInt("goods_id"),
                            goodsName = detailData.getJSONObject(i).getString("goods_name"),
                            goodsQuantity = detailData.getJSONObject(i).getDouble("goods_quantity"),
                            goodsUnit = detailData.getJSONObject(i).getString("goods_unit"),
                            goodsPrice = detailData.getJSONObject(i).getInt("goods_price")
                        ))
                    }

                    warehouseResponse = WarehouseResponse(
                        status = statusCode,
                        message = message,
                        totalData = totalData,
                        goods = goods
                    )

                    warehouse.postValue(warehouseResponse)
                } else {
                    warehouseResponse = WarehouseResponse(
                        status = statusCode,
                        message = message,
                        totalData = totalData,
                        goods = ArrayList()
                    )

                    warehouse.postValue(warehouseResponse)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                // Parse response to WarehouseResponse
                val result = String(responseBody)
                val response = JSONObject(result)

                Log.d("API", result)

                val message = response.getString("message")

                warehouseResponse = WarehouseResponse(
                    status = statusCode,
                    message = message,
                    totalData = 0,
                    goods = ArrayList()
                )

                warehouse.postValue(warehouseResponse)
            }
        })

        return warehouseResponse
    }

    fun loadGoods(): LiveData<WarehouseResponse> = warehouse

}