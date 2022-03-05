package com.example.musicplayer.data.api

import com.example.musicplayer.data.model.Lyric
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsAPI {
    @GET("{artist}/{title}")
    suspend fun search(
        @Path("artist") artist: String,
        @Path("title") title: String
    ): Response<Lyric>
}