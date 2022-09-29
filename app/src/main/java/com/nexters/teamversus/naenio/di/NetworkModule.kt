package com.nexters.teamversus.naenio.di

import com.nexters.teamversus.naenio.data.network.ApiProvider
import com.nexters.teamversus.naenio.data.network.api.CommentApi
import com.nexters.teamversus.naenio.data.network.api.FeedApi
import com.nexters.teamversus.naenio.data.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideUserApi(): UserApi {
        return ApiProvider.retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFeedApi(): FeedApi {
        return ApiProvider.retrofit.create(FeedApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCommentApi(): CommentApi {
        return ApiProvider.retrofit.create(CommentApi::class.java)
    }
}
