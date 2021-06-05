package com.example.infinitestock.data.entity

data class WarehouseResponse(
    var status: Int = 0,
    var message: String? = null,
    var totalData: Int = 0,
    var goods: ArrayList<Good>
)
