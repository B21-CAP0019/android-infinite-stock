package com.example.infinitestock.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Good(
    var goodsId: Int = 0,
    var goodsName: String? = null,
    var goodsQuantity: Double? = null,
    var goodsUnit: String? = null,
    var goodsPrice: Int = 0
): Parcelable
