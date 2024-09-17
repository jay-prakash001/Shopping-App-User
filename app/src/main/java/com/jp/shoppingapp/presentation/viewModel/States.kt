package com.jp.shoppingapp.presentation.viewModel

import com.jp.shoppingapp.domain.model.BannerModel
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.domain.model.UserDataParent
import com.jp.shoppingappadmin.domain.model.CategoryModel


data class SignUpState(
    val isLoading: Boolean = false,
    val success: String = "",
    val error: String = ""
)

data class GetUser(
    val isLoading: Boolean = false,
    val error: String = "",
    val userDataParent: UserDataParent? = null
)

data class LoginState(
    val isLoading: Boolean = false,
    val success: String = "",
    val error: String = "",
    val userData: String? = null
)

data class GetCategoriesState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<CategoryModel> = emptyList()
)

data class ProductsState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<ProductModel> = emptyList()
)

data class BannerState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<BannerModel> = emptyList()
)
data class WishListState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<ProductModel> = emptyList()
)
data class CartState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<OrderModel> = emptyList()
)

data class State<out T>(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<T?>? = null
)