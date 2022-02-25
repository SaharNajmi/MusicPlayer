package com.example.musicplayer.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player
import kotlinx.coroutines.launch

class MainViewModel(val player: Player, val musicRepository: MusicRepository) : ViewModel() {
    var song = player.song
    var playerState = player.playerState
    val progress
        get() = player.progress

    val songPosition
        get() = player.songPosition

    val duration
        get() = player.duration

    val musics = liveData {
        val result = musicRepository.getMusics()
        player.updateList(result)
        emit(result)
    }

    val databaseExists = liveData {
        val result = musicRepository.databaseExists()
        emit(result)
    }

    fun changeSongPosition(position: Int) {
        player.songPosition = position
    }

    fun changeSong(song: Song) {
        player.song.value = song
    }

    fun insertMusics() = viewModelScope.launch { musicRepository.insertMusics() }

    fun insertAlbums() = viewModelScope.launch { musicRepository.insertAlbums() }

    fun insertArtists() = viewModelScope.launch { musicRepository.insertArtists() }

    fun toggleState() = player.toggleState()

    fun pauseSong() = player.pauseSong()

    fun nextSong() = player.nextSong()

    fun backSong() = player.backSong()
}

