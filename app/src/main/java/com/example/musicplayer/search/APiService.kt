package com.example.musicplayer.search

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APiService {
    const val BASE_URL = "https://private-anon-75d190a1d2-lyricsovh.apiary-proxy.com/v1/"

    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val api: LyricsAPI by lazy {
        retrofit().create(LyricsAPI::class.java)
    }
}