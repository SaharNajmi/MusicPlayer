package com.example.musicplayer.detail

import androidx.lifecycle.ViewModel
import com.example.musicplayer.player.Player

class DetailViewModel(val player: Player) : ViewModel() {
    var songModel = player.songModel
    var playerState = player.playerState
    var progress = player.progress

    fun getRepeat() = player.isRepeat

    fun getShuffle() = player.isShuffle

    fun changeIsRepeat(): Boolean {
        player.isRepeat = !player.isRepeat
        return player.isRepeat
    }

    fun changeIsShuffle(): Boolean {
        player.isShuffle = !player.isShuffle
        return player.isShuffle
    }

    fun getNewSongDuration() = player.duration

    fun toggleState() = player.toggleState()

    fun nextSong() = player.nextSong()

    fun backSong() = player.backSong()

    fun playerSeekTo(newSeek: Int) = player.playerSeekTo(newSeek)
}