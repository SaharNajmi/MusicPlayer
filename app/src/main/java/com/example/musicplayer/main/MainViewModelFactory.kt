package com.example.musicplayer.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.player.Player

class MainViewModelFactory(val player: Player) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(player) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}