package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.domain.model.User
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repo: Repo) {

    fun loginWithEmailPassword(email: String, password: String) :Flow<ResultState<String>>{
       return repo.loginWithEmailPassword(email, password)
    }


}