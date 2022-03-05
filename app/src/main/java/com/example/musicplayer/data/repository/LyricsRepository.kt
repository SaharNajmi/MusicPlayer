package com.example.musicplayer.data.repository

import com.example.musicplayer.data.api.LyricsAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LyricsRepository @Inject constructor(private val apiService: LyricsAPI) {
    suspend fun searchLyrics(artist: String, title: String) =
        withContext(Dispatchers.IO) { apiService.search(artist, title) }
}