package com.jp.shoppingapp.domain.useCases

import com.jp.shoppingapp.domain.model.User
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repo: Repo) {
    fun createUser(user: User): Flow<ResultState<String>> {
        return repo.registerUserWithEmailAndPassword(user)
    }

    suspend fun updateUser(
        name: String,
        email: String,
        phone: String,
        uid: String,
        profileImg: String
    ): Flow<ResultState<String>> {
        return repo.updateUser(name = name, email = email, phone = phone,profileImg= profileImg, uid = uid)
    }
}