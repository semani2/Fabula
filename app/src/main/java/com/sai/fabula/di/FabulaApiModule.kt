package com.sai.fabula.di

import com.sai.fabula.api.FabulaApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
class FabulaApiModule {

    @Provides
    fun getFabulaApiService(): FabulaApiService = Retrofit.Builder()
        .baseUrl(FabulaApiService.NEWS_API_URL)
        .addConverterFactory(MoshiConverterFactory.create(
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        ))
        .build()
        .create(FabulaApiService::class.java)
}
