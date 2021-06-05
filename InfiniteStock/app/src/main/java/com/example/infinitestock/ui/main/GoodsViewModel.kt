package com.example.infinitestock.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinitestock.R
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.data.entity.WarehouseResponse
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class GoodsViewModel: ViewModel() {

    private val warehouseResponse = MutableLiveData<WarehouseResponse>()

    fun retrieveGoods(context: Context, account: Account?) {
        val url = context.resources.getString(R.string.server) + "/warehouse/get/goods/all"
        val client = AsyncHttpClient()

        client.addHeader("x-access-publicid", account?.publicId)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val response = JSONObject(result)

                // TODO: Parse response to WarehouseResponse
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                // TODO: Parse response to WarehouseResponse
            }

        })
    }

    fun loadGoods(): LiveData<WarehouseResponse> = warehouseResponse

}