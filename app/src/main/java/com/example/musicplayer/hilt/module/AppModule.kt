package com.example.musicplayer.hilt.module

import android.app.Application
import androidx.room.Room
import com.example.musicplayer.data.api.LyricsAPI
import com.example.musicplayer.data.db.MusicDatabase
import com.example.musicplayer.data.db.dao.MusicDao
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.player.Player
import com.example.musicplayer.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMusicDatabase(application: Application): MusicDatabase = Room.databaseBuilder(
        application,
        MusicDatabase::class.java,
        "music_database"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideMusicDao(musicDatabase: MusicDatabase): MusicDao {
        return musicDatabase.musicDao()
    }

    @Provides
    @Singleton
    fun providePlayer() = Player()

    @Provides
    @Singleton
    fun provideLocalMusic(application: Application) = LocalMusic(application)

    @Singleton
    @Provides
    fun provideApiService(): LyricsAPI = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()
        .create(LyricsAPI::class.java)

}