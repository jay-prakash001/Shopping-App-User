package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.common.CART_COLLECTION
import com.jp.shoppingapp.common.ORDER_COLLECTION
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.OrderParentModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderUseCase @Inject constructor(val repo: Repo) {
    fun getOrders(): Flow<ResultState<List<OrderParentModel>>> {
        return repo.getOrders(ORDER_COLLECTION)
    }
    suspend fun <T : Any>addOrders(orderModel: T ,collection : String): Flow<ResultState<String>> {
        return repo.addOrder(orderModel, collection = collection)
    }

  suspend fun deleteFromCart(orderModel: OrderModel, collection: String = CART_COLLECTION): Flow<ResultState<String>> {
      println("ADDORDER del $orderModel")
      return repo.deleteCart(orderModel, collection)
  }

    fun getProductsFromCart(collection: String = CART_COLLECTION): Flow<ResultState<List<OrderModel>>> {
        return repo.getCartOrders(collection)
    }

    suspend fun cancelOrder(orderParentModel: OrderParentModel, status : String = "Cancelled"): Flow<ResultState<String>> {
        return repo.updateOrderStatus(orderParentModel, status)
    }
}