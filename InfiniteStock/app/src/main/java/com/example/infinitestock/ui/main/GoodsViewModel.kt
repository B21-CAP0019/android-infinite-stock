package com.example.infinitestock.ui.main

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.data.entity.Good
import com.example.infinitestock.data.entity.WarehouseResponse
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class GoodsViewModel: ViewModel() {

    private val warehouse = MutableLiveData<WarehouseResponse>()

    fun retrieveGoods(context: Context, account: Account?, async: Boolean = true) : WarehouseResponse {
        try {
            Looper.prepare()
        } catch (ignored: Exception) {
        }
        val url = context.resources.getString(R.string.server) + "/warehouse/goods/get/all"
        val client = if (async) AsyncHttpClient() else SyncHttpClient()

        var warehouseResponse = WarehouseResponse(
            status = 0,
            message = "",
            totalData = 0,
            goods = ArrayList()
        )

        client.addHeader("x-access-publicid", account?.publicId)
        client.get(url, object : AsyncHttpResponseHandler() {
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
                            goodsId = detailData.getJSONArray(i).getInt(0),
                            goodsName = detailData.getJSONArray(i).getString(1),
                            goodsQuantity = detailData.getJSONArray(i).getDouble(2),
                            goodsUnit = detailData.getJSONArray(i).getString(3),
                            goodsPrice = detailData.getJSONArray(i).getInt(4)
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