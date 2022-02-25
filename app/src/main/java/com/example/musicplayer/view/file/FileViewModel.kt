package com.example.musicplayer.view.file

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.repository.MusicRepository
import kotlinx.coroutines.Dispatchers

class FileViewModel(val musicRepository: MusicRepository) : ViewModel() {

    val fileNames = liveData(Dispatchers.IO) {
        val result = musicRepository.getFileNames()
        emit(result)
    }
}