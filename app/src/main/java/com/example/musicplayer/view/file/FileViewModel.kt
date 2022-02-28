package com.example.musicplayer.view.file

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(val musicRepository: MusicRepository) : ViewModel() {

    val fileNames = liveData(Dispatchers.IO) {
        val result = musicRepository.getFileNames()
        emit(result)
    }
}