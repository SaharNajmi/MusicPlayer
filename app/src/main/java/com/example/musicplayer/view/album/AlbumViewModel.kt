package com.example.musicplayer.view.album

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.repository.MusicRepository

class AlbumViewModel(
    val musicRepository: MusicRepository
) : ViewModel() {
    fun getAlbums() = musicRepository.getAlbums()
}