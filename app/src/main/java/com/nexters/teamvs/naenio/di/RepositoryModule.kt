package com.nexters.teamvs.naenio.di

import com.nexters.teamvs.naenio.data.network.api.CommentApi
import com.nexters.teamvs.naenio.data.network.api.FeedApi
import com.nexters.teamvs.naenio.repository.UserRepository
import com.nexters.teamvs.naenio.data.network.api.UserApi
import com.nexters.teamvs.naenio.repository.CommentRepository
import com.nexters.teamvs.naenio.repository.FeedRepository
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
    ): UserRepository {
        return UserRepository(userApi)
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