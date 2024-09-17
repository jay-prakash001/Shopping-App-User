package com.jp.shoppingapp.domain.repo

import android.net.Uri
import com.jp.shoppingapp.common.ORDER_COLLECTION
import com.jp.shoppingapp.domain.model.BannerModel
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.OrderParentModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.domain.model.User
import com.jp.shoppingapp.domain.model.UserDataParent
import com.jp.shoppingappadmin.common.ResultState
import com.jp.shoppingappadmin.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerUserWithEmailAndPassword(user: User): Flow<ResultState<String>>
    fun loginWithEmailPassword(email: String, password: String): Flow<ResultState<String>>

    suspend fun updateUser(
        name: String,
        email: String,
        phone: String,
        uid: String,
        profileImg: String
    ): Flow<ResultState<String>>

    fun getUserByUid(uid: String): Flow<ResultState<UserDataParent>>
    suspend fun uploadMedia(uri: Uri): Flow<ResultState<String>>

    fun getCategories(): Flow<ResultState<List<CategoryModel>>>
    fun searchProducts(name: String, fieldName: String): Flow<ResultState<List<ProductModel>>>
    fun searchCategories(name: String): Flow<ResultState<List<CategoryModel>>>
    fun getAllProducts(): Flow<ResultState<List<ProductModel>>>
    fun getBanners(): Flow<ResultState<List<BannerModel>>>

    suspend fun addToWishList(
        productModel: ProductModel,
        collection: String
    ): Flow<ResultState<String>>

    suspend fun deleteFromWishList(
        productModel: ProductModel,
        collection: String
    ): Flow<ResultState<String>>

    fun getProductsFromWishList(collection: String): Flow<ResultState<List<ProductModel>>>

    suspend fun <T : Any> addOrder(orderModel: T, collection: String): Flow<ResultState<String>>
    suspend fun deleteOrder(
        orderModel: OrderParentModel,
        collection: String
    ): Flow<ResultState<String>>

    suspend fun deleteCart(orderModel: OrderModel, collection: String): Flow<ResultState<String>>

    suspend fun updateOrderStatus(orderParentModel: OrderParentModel, status : String): Flow<ResultState<String>>

    fun getCartOrders(collection: String = ORDER_COLLECTION): Flow<ResultState<List<OrderModel
            >>>

    fun getOrders(collection: String = ORDER_COLLECTION): Flow<ResultState<List<OrderParentModel
            >>>


}