package com.nexters.teamvs.naenio.di

import com.nexters.teamvs.naenio.data.network.api.CommentApi
import com.nexters.teamvs.naenio.data.network.api.FeedApi
import com.nexters.teamvs.naenio.data.network.api.UserApi
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.utils.datastore.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideUserRepository(
        userApi: UserApi,
        userPreferencesRepository: UserPreferencesRepository
    ): UserRepository {
        return UserRepository(userApi, userPreferencesRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFeedRepository(
        feedApi: FeedApi,
    ): FeedRepository {
        return FeedRepository(feedApi)
    }

    @Provides
    @ViewModelScoped
    fun provideCommentRepository(
        commentApi: CommentApi,
    ): CommentRepository {
        return CommentRepository(commentApi)
    }
}