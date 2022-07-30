package com.nexters.teamvs.naenio.di

import com.nexters.teamvs.naenio.data.network.ApiProvider
import com.nexters.teamvs.naenio.data.network.api.NaenioApi
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
    fun provideNaenioApi(): NaenioApi {
        return ApiProvider.retrofit.create(NaenioApi::class.java)
    }
}
