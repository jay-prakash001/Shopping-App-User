package com.jp.shoppingapp.domain.di

import androidx.activity.compose.ReportDrawn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jp.shoppingapp.data.repo.RepoImpl
import com.jp.shoppingapp.domain.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideRepo(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore, firebaseStorage: FirebaseStorage): Repo {
        return RepoImpl(firebaseAuth, firebaseFirestore, firebaseStorage)
    }


}