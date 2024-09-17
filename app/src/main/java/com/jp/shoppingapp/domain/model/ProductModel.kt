package com.jp.shoppingapp.domain.model

data class ProductModel(
    var id: String = "",
    var name: String = "",
    var date: Long = System.currentTimeMillis(),
    var category: String = "",
    var price: Long = 0,
    var desc: String = "",
    var images: List<String> = emptyList()
)