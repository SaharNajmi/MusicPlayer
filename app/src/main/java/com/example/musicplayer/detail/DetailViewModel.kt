package com.example.musicplayer.detail

import androidx.lifecycle.ViewModel
import com.example.musicplayer.player.Player

class DetailViewModel(val player: Player) : ViewModel() {
    var isRepeat: Boolean = player.isRepeat
    val isShuffle: Boolean = player.isShuffle
    var songModel = player.songModel
    var playerState = player.playerState
    var progress = player.progress

    fun changeIsRepeat(isRepeat: Boolean) {
        if (isRepeat)
            player.isRepeat = false
        else {
            player.isRepeat = true
            player.isShuffle = false
        }
    }

    fun changeIsShuffle(isShuffle: Boolean) {
        if (isShuffle)
            player.isShuffle = false
        else {
            player.isShuffle = true
            player.isRepeat = false
        }
    }

    fun getNewSongDuration() = player.duration

    fun toggleState() = player.toggleState()

    fun nextSong() = player.nextSong()

    fun backSong() = player.backSong()

    fun playerSeekTo(newSeek: Int) = player.playerSeekTo(newSeek)
}