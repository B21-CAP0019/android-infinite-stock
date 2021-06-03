package com.example.infinitestock.api

data class Goods(
	val data: Data? = null,
	val message: String? = null,
	val status: Int? = null
)

data class Data(
	val totalData: Int? = null,
	val detailData: List<Int?>? = null
)

