package com.example.musicplayer.view.search

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
    val lyrics = MutableLiveData<Lyric>()
    val findLyrics = MutableLiveData(false)

/*    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Log.e("Error->", throwable.message.toString())
            lyrics.value=Lyric(null, "Network error, please try again")
            findLyrics.value=false
        }*/

    fun searchLyrics(artist: String, title: String) {
        viewModelScope.launch {
            try {
                lyrics.value = lyricsRepository.searchLyrics(artist, title)
                findLyrics.value = true
            } catch (throwable: Throwable) {
                lyrics.value = Lyric(null, "Not found!!!")
                findLyrics.value = false
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