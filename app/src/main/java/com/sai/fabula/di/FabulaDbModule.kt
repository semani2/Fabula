package com.sai.fabula.di

import android.content.Context
import com.sai.fabula.FabulaApp
import com.sai.fabula.database.FabulaNewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class FabulaDbModule {

    @Singleton
    @Provides
    fun getNewsDatabase(@ApplicationContext context: Context) = FabulaNewsDatabase.getInstance(context)
}
