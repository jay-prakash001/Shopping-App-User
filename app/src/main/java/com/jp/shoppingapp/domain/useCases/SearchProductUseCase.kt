package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(val repo: Repo) {

    fun search(name: String, fieldName: String): Flow<ResultState<List<ProductModel>>> {
        return repo.searchProducts(name,fieldName = fieldName)
    }

    fun getProducts() : Flow<ResultState<List<ProductModel>>>{
        return repo.getAllProducts()
    }
}