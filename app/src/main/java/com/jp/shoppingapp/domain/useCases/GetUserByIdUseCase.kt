package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.domain.model.UserDataParent
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(val repository: Repo) {

    fun getUserById(uId : String) : Flow<ResultState<UserDataParent>>{
        return repository.getUserByUid(uId)
    }
}