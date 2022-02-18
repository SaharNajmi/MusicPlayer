package com.example.musicplayer.view.main

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player

class MainViewModel(val player: Player, val musicRepository: MusicRepository) : ViewModel() {
    var song = player.song
    var playerState = player.playerState
    val progress
        get() = player.progress

    val songPosition
        get() = player.songPosition

    val duration
        get() = player.duration

    fun changeSongPosition(position: Int) {
        player.songPosition = position
    }

    fun changeSong(song: Song) {
        player.song.value = song
    }

    fun insertMusics() = musicRepository.insertSongs()

    fun insertAlbums() = musicRepository.insertAlbums()

    fun insertArtists() = musicRepository.insertArtists()

    fun musics() = musicRepository.getMusics()

    fun toggleState() = player.toggleState()

    fun pauseSong() = player.pauseSong()

    fun nextSong() = player.nextSong()

    fun backSong() = player.backSong()
}

