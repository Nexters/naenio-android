package com.nexters.teamvs.naenio.di

import com.nexters.teamvs.naenio.repository.UserRepository
import com.nexters.teamvs.naenio.data.network.api.NaenioApi
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