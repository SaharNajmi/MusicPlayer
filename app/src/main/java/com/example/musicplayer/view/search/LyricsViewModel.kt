package com.example.musicplayer.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.model.Lyric
import com.example.musicplayer.data.repository.LyricsRepository
import com.example.musicplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val lyricsRepository: LyricsRepository
) : ViewModel() {

    private val _lyrics = MutableLiveData<Lyric>()
    val lyrics: LiveData<Lyric> = _lyrics
    var loading = MutableLiveData<Boolean>()

    fun searchLyrics(artist: String, title: String) {
        loading.value = true

        viewModelScope.launch() {
            val response = lyricsRepository.searchLyrics(artist, title)
            if (response.isSuccessful) {
                _lyrics.value = response.body()
                loading.value = false
            } else {
                loading.value = false
                val lyrics = Lyric(lyrics = null, error = "No lyrics found")
                _lyrics.value = lyrics
            }
        }
    }

    fun update(song: Song) {
        song.isLyrics = true
        viewModelScope.launch {
            musicRepository.update(song)
        }
    }
}