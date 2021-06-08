package com.example.infinitestock.ui.stock.entrystock

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.ReportItem
import com.example.infinitestock.data.entity.HistoryResponse
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class EntryStockViewModel : ViewModel() {
    private var reportItems = MutableLiveData<HistoryResponse>()
    // var tempString: String = ""

    fun retrieveEntryReport(context: Context, async: Boolean = true): HistoryResponse {
        try {
            Looper.prepare()
        } catch (ignored: Exception) {
        }
        var reportResponse = HistoryResponse()

        val account = SessionCompat(context).getAccount()
        val url = context.resources.getString(R.string.server) + "/warehouse/goods/report/goodsin"
        val client = if (async) AsyncHttpClient() else SyncHttpClient()

        val params = RequestParams()
        params.put("public_id", account.publicId)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                // Parsing JSON
                if (statusCode == 200) {
                    val result = String(responseBody)
                    val response = JSONObject(result)

                    Log.d("API", result)

                    // Parse response to WarehouseResponse
                    val data = response.getJSONArray("data")

                    val items = ArrayList<ReportItem>()
                    if (data is JSONArray) {
                        for (i in 0 until data.length()) {
                            items.add(
                                ReportItem(
                                    dateTime = data.getJSONObject(i).getString("datetime"),
                                    name = data.getJSONObject(i).getString("goods_name"),
                                    qty = data.getJSONObject(i).getDouble("goods_quantity"),
                                    unit = data.getJSONObject(i).getString("goods_unit")
                                )
                            )
                        }

                        reportResponse = HistoryResponse(
                            status = statusCode,
                            message = "",
                            totalData = 0,
                            reportItems = items
                        )
                    } else {
                        reportResponse = HistoryResponse(
                            status = statusCode,
                            message = "",
                            totalData = 0,
                            reportItems = ArrayList()
                        )
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                Log.d("EntryStockViewModel", "errorCode $statusCode")
                reportResponse = HistoryResponse(
                    status = statusCode,
                    message = "",
                    totalData = 0,
                    reportItems = ArrayList()
                )
            }
        })
        return reportResponse
    }

    fun getItems(): LiveData<HistoryResponse> = reportItems

    /*
    fun getAllItems(): ArrayList<StockItem> {
        return items
    }

    fun getItem(string: String): StockItem {
        return items[searchId(string)]
    }

    fun addItem(item: StockItem) {
        if (searchId(item.name) == -1) {
            item.stockId = items.size
            items.add(item)
        } else {
            editItem(item.name, item)
        }
        tempString = ""
    }

    fun editItem(string: String, item: StockItem) {
        val id = searchId(string)
        if (id == -1){
            addItem(item)
        } else {
            items.set(id, item)
        }
        tempString = ""
    }

    fun removeItem(item: StockItem) {
        val id = searchId(item.name)
        if (id != -1) {
            items.removeAt(id)
        }
    }

    private fun searchId(string: String): Int {
        var indeks = 0
        for (item in items){
            if (item.name == string) {
                return indeks
            }
            indeks += 1
        }
        return -1
    }
    */
}