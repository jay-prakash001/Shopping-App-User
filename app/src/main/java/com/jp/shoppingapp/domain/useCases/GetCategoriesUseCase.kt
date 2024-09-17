package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import com.jp.shoppingappadmin.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(val repo: Repo) {

    fun getCategories(): Flow<ResultState<List<CategoryModel>>> {
        return repo.getCategories()
    }
    fun searchCategories(name:String): Flow<ResultState<List<CategoryModel>>> {
        return repo.searchCategories(name)
    }
}