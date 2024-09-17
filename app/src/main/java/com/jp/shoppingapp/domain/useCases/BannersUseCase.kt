package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.domain.model.BannerModel
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow

class BannersUseCase(val repo: Repo) {

    suspend fun getAllBanners(): Flow<ResultState<List<BannerModel>>> {
        return repo.getBanners()
    }
}