package com.jp.shoppingappadmin.domain.model

data class CategoryModel(
    var id :String = "",
    var name : String = "",
    var date : Long = System.currentTimeMillis(),
    var createdBy :String = "",
    var img : String = ""


)