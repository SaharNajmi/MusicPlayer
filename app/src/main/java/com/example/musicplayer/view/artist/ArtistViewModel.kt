package com.example.musicplayer.view.artist

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.repository.MusicRepository

class ArtistViewModel(val musicRepository: MusicRepository) : ViewModel() {
    fun getArtists() = musicRepository.getArtists()
}