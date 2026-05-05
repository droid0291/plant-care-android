package com.plantcare.app.di

import com.plantcare.app.data.remote.PlantApiService
import com.plantcare.app.data.repository.PlantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePlantRepository(apiService: PlantApiService): PlantRepository =
        PlantRepository(apiService)
}
