package com.example.infinitestock.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account(
    var publicId: String? = null
) : Parcelable
