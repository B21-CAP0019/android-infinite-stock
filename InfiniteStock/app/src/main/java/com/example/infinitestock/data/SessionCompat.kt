package com.example.infinitestock.data

import android.content.Context
import com.example.infinitestock.data.entity.Account

class SessionCompat(context: Context) {
    companion object {
        private const val PREFERENCE_NAME = "session_preference"
        private const val PUBLIC_ID = "public_id"
    }

    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getPublicId() : Account {
        val publicId = preference.getString(PUBLIC_ID, "")
        return Account(
            publicId = publicId
        )
    }

    fun setPublicId(account: Account) {
        val publicId = account.publicId

        with (preference.edit()) {
            putString(PUBLIC_ID, publicId)
            apply()
        }
    }
}