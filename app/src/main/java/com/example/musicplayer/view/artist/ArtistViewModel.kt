package com.example.musicplayer.view.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.repository.MusicRepository
import kotlinx.coroutines.Dispatchers

class ArtistViewModel(val musicRepository: MusicRepository) : ViewModel() {
    val artists = liveData(Dispatchers.IO) {
        val result = musicRepository.getArtists()
        emit(result)
    }
}