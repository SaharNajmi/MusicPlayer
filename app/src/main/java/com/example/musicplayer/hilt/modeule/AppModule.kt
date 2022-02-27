package com.example.musicplayer.hilt.modeule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    // @Named("name")
    fun provideString(): String = "Sahar"

/*    @Provides
    @Singleton
    @Named("family")
    fun provideString2(): String = "Najmi"*/
}