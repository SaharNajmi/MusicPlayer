package com.example.musicplayer.view.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    val musicRepository: MusicRepository
) : ViewModel() {
    val albums = liveData(Dispatchers.IO) {
        val result = musicRepository.getAlbums()
        emit(result)
    }
}