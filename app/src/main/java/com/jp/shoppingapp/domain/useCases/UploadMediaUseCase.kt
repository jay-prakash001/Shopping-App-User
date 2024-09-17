package com.jp.shoppingapp.domain.useCases

import android.net.Uri
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class UploadMediaUseCase @Inject constructor(val repo: Repo) {
    suspend fun uploadMedia(uri: Uri): Flow<ResultState<String>> {
        return repo.uploadMedia(uri)
    }
}