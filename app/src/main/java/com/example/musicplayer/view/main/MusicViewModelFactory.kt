package com.example.musicplayer.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MusicViewModelFactory(val repository: MusicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MusicViewModel::class.java)
            return MusicViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
