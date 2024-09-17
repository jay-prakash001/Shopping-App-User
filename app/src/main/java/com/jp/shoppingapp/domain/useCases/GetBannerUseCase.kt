package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.domain.model.BannerModel
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBannerUseCase @Inject constructor(val repo: Repo) {
    fun getBanners(): Flow<ResultState<List<BannerModel>>> {
        return repo.getBanners()
    }
}