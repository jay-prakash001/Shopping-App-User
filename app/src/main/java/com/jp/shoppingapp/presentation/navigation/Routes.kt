package com.jp.shoppingapp.presentation.navigation


import com.jp.shoppingapp.domain.model.ProductModel
import kotlinx.serialization.Serializable

sealed class Routes {


    @Serializable
    data object Login : Routes()

    @Serializable
    data object SignUp : Routes()

    @Serializable
    data object Dashboard : Routes()

    @Serializable
    data object Cart : Routes()

    @Serializable
    data object Orders : Routes()

    @Serializable
    data object WishList : Routes()

    @Serializable
    data object Profile : Routes()

    @Serializable
    data class ProductDetail(var id: String = "") : Routes()


    @Serializable
    data class CheckOut(var id: String = "", var quantity: Int = 0) : Routes()

    @Serializable
    data object Categories : Routes()

    @Serializable
    data class ProductsScreen(val category: String) : Routes()

    @Serializable
    data class SearchProducts(val name: String) : Routes()


}

sealed class SubNavigation {
    @Serializable
    object LoginSignUp : SubNavigation()

    @Serializable
    object MainHome : SubNavigation()
}