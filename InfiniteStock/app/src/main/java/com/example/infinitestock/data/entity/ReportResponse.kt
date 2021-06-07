package com.example.infinitestock.data.entity

data class ReportResponse(
    var status: Int = 0,
    var message: String? = null,
    var totalData: Int = 0,
    var reportItems: ArrayList<ReportItem> = ArrayList()
)
