package com.example.musicplayer.view.all

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.player.Player

class HomeViewModel(val player: Player) : ViewModel() {
    fun getMusics(context: Context) = player.getSongs(context)
}