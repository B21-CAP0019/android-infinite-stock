package com.example.infinitestock.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportItem(
    var dateTime: String = "",
    var name: String = "",
    var qty: String = "",
    var unit: String = ""
): Parcelable
