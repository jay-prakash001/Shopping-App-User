package com.jp.shoppingapp.domain.model


data class User(
    var id :String = "",
    var name : String = "" ,
    var profileImg : String = "",
    var email : String  = "",
    var phone : String = "",
    var password : String  = ""
)
data class UserDataParent(val nodeId : String, val user : User?)