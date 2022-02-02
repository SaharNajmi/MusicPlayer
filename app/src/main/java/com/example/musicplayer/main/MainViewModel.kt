package com.example.musicplayer.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.player.Player

class MainViewModel(val player: Player) : ViewModel() {
    var songModel = player.songModel
    var playerState = player.playerState
    var progress = player.progress
    val songPosition = player.songPosition

    fun getMusics(context: Context) = player.getSongs(context)

    fun getNewSongDuration() = player.duration

    fun toggleState() = player.toggleState()

    fun nextSong() = player.nextSong()

    fun backSong() = player.backSong()
}

