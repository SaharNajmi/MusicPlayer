package com.example.musicplayer.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.detail.DetailViewModel

class DetailViewModelFactory(val player: Player) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(player) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}