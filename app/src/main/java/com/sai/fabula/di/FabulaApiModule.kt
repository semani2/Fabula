package com.sai.fabula.di

import com.sai.fabula.api.FabulaApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FabulaApiModule {

    fun getFabulaApiService(): FabulaApiService = Retrofit.Builder()
        .baseUrl(FabulaApiService.NEWS_API_URL)
        .addConverterFactory(MoshiConverterFactory.create(
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        ))
        .build()
        .create(FabulaApiService::class.java)
}
