package com.example.musicplayer.view.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.repository.MusicRepository
import kotlinx.coroutines.Dispatchers

class AlbumViewModel(
    val musicRepository: MusicRepository
) : ViewModel() {
    val albums = liveData(Dispatchers.IO) {
        val result = musicRepository.getAlbums()
        emit(result)
    }
}