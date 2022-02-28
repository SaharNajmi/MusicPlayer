package com.example.musicplayer.view.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(val musicRepository: MusicRepository) : ViewModel() {
    val artists = liveData(Dispatchers.IO) {
        val result = musicRepository.getArtists()
        emit(result)
    }
}