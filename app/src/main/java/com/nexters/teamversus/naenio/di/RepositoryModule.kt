package com.nexters.teamversus.naenio.di

import com.nexters.teamversus.naenio.data.UserRepository
import com.nexters.teamversus.naenio.data.network.api.NaenioApi
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
        naenioApi: NaenioApi,
    ): UserRepository {
        return UserRepository(naenioApi)
    }
}