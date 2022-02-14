package com.example.musicplayer.view.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.player.Player

class MainViewModel(val player: Player) : ViewModel() {
    var songModel = player.songModel
    var playerState = player.playerState
    var progress = player.progress

    fun changeSongPosition(position: Int) {
        player.songPosition = position
    }

    fun changeSongModel(songModel: SongModel) {
        player.songModel.value = songModel
    }

    fun getSongPosition() = player.songPosition

    fun getValueProgress() = player.progress.value

    fun getDuration() = player.duration

    fun getMusics(context: Context) = player.getSongs(context)

    fun getNewSongDuration() = player.duration

    fun toggleState() = player.toggleState()

    fun pauseSong() {
        player.pauseSong()
    }

    fun nextSong() = player.nextSong()

    fun backSong() = player.backSong()
}

