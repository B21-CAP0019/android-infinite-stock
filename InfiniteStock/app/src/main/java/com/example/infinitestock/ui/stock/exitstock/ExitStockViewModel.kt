package com.example.infinitestock.ui.stock.exitstock

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.ReportItem
import com.example.infinitestock.data.entity.ReportResponse
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ExitStockViewModel : ViewModel() {

    private val liveReportResponse = MutableLiveData<ReportResponse>()

    fun getReportResponse(): LiveData<ReportResponse> = liveReportResponse

    fun retrieveResponses(context: Context, async: Boolean = true): ReportResponse {
        try {
            Looper.prepare()
        } catch (e: Exception) {
            /* no-op */
        }

        var reportResponse = ReportResponse()
        val account = SessionCompat(context).getAccount()
        val url = context.resources.getString(R.string.server) + "/warehouse/goods/report/goodsout"
        val client = if (async) AsyncHttpClient() else SyncHttpClient()

        val params = RequestParams()
        params.put("public_id", account.publicId)

        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                if (statusCode == 200) {
                    val result = String(responseBody)
                    Log.d("API", result)
                    val response = JSONObject(result)

                    val status = response.getInt("status")
                    val message = response.getString("message")

                    val data = response.getJSONArray("data")
                    val list = ArrayList<ReportItem>()
                    for (i in 0 until data.length()) {
                        with (data.getJSONObject(i)) {
                            list.add(ReportItem(
                                dateTime = this.getString("datetime"),
                                name = this.getString("goods_name"),
                                qty = this.getDouble("goods_quantity"),
                                unit = this.getString("goods_unit")
                            ))
                        }
                    }
                    reportResponse = ReportResponse(
                        status = statusCode,
                        totalData = if (status == 1) data.length() else 0,
                        message = message,
                        reportItems = if (data.length() > 0) list else ArrayList()
                    )
                    liveReportResponse.postValue(reportResponse)
                } else {
                    reportResponse = ReportResponse(
                        status = statusCode,
                        totalData = 0,
                        message = "Unknown response",
                        reportItems = ArrayList()
                    )
                    liveReportResponse.postValue(reportResponse)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                if (statusCode < 500) {
                    val result = String(responseBody)
                    val response = JSONObject(result)

                    val message = response.getString("message")

                    reportResponse = ReportResponse(
                        status = statusCode,
                        totalData = 0,
                        message = message,
                        reportItems = ArrayList()
                    )
                    liveReportResponse.postValue(reportResponse)
                }
            }

        })
        return reportResponse
    }
}