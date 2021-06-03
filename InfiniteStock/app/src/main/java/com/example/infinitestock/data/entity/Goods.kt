package com.example.infinitestock.data.entity

data class Goods(
    var goodsId: String? = null,
    var goodsName: String? = null,
    var goodsQuantity: Double? = null,
    var goodsUnit: String? = null,
    var goodsPrice: Int = 0
)
