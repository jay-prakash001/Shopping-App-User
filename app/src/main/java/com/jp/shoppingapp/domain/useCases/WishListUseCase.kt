package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.common.CART_COLLECTION
import com.jp.shoppingapp.common.WISHLIST
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishListUseCase @Inject constructor(val repo: Repo) {

    suspend fun addToCart(productModel: ProductModel): Flow<ResultState<String>> {
        return repo.addToWishList(productModel, CART_COLLECTION)
    }

    suspend fun deleteFromCart(productModel: ProductModel): Flow<ResultState<String>> {
        return repo.deleteFromWishList(productModel, CART_COLLECTION)
    }

     fun getProductsFromCart(): Flow<ResultState<List<ProductModel>>> {
        return repo.getProductsFromWishList(CART_COLLECTION)
    }
    suspend fun addToWishList(productModel: ProductModel): Flow<ResultState<String>> {
        return repo.addToWishList(productModel, WISHLIST)
    }

    suspend fun deleteFromWishList(productModel: ProductModel): Flow<ResultState<String>> {
        return repo.deleteFromWishList(productModel, WISHLIST)
    }

    fun getProductsFromWishList(): Flow<ResultState<List<ProductModel>>> {
        return repo.getProductsFromWishList(WISHLIST)
    }
}