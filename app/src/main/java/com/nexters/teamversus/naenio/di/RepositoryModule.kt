package com.nexters.teamversus.naenio.di

import com.nexters.teamversus.naenio.data.network.api.CommentApi
import com.nexters.teamversus.naenio.data.network.api.FeedApi
import com.nexters.teamversus.naenio.data.network.api.UserApi
import com.nexters.teamversus.naenio.domain.repository.CommentRepository
import com.nexters.teamversus.naenio.domain.repository.FeedRepository
import com.nexters.teamversus.naenio.domain.repository.UserRepository
import com.nexters.teamversus.naenio.utils.datastore.UserPreferencesRepository
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
        feedApi: FeedApi,
        userPreferencesRepository: UserPreferencesRepository
    ): CommentRepository {
        return CommentRepository(
            commentApi = commentApi,
            feedApi = feedApi,
            userPreferencesRepository = userPreferencesRepository
        )
    }
}