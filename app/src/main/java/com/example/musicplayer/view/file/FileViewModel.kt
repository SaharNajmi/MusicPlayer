package com.example.musicplayer.view.file

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.repository.MusicRepository

class FileViewModel(val musicRepository: MusicRepository) : ViewModel() {
    fun getFileNames() = musicRepository.getFileNames()
}