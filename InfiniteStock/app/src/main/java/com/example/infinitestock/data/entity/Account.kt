package com.example.infinitestock.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account(
    var fullName: String? = null,
    var publicId: String? = null,
    var shopName: String? = null
) : Parcelable
