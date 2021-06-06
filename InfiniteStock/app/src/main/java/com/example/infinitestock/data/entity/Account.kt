package com.example.infinitestock.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account(
    var publicId: String? = null,
    var fullName: String? = null,
    var shopName: String? = null
) : Parcelable
