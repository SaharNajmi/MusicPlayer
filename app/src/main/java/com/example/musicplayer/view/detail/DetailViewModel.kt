package com.example.musicplayer.view.detail

import androidx.lifecycle.ViewModel
import com.example.musicplayer.player.Player

class DetailViewModel(val player: Player) : ViewModel() {
    var song = player.song
    var playerState = player.playerState
    var progress = player.progress
    val shuffle
        get() = player.isShuffle

    val repeat
        get() = player.isRepeat

    fun changeIsRepeat(): Boolean {
        player.isRepeat = !player.isRepeat
        return player.isRepeat
    }

    fun changeIsShuffle(): Boolean {
        player.isShuffle = !player.isShuffle
        return player.isShuffle
    }

    fun toggleState() = player.toggleState()

    fun nextSong() = player.nextSong()

    fun backSong() = player.backSong()

    fun playerSeekTo(newSeek: Int) = player.playerSeekTo(newSeek)
}