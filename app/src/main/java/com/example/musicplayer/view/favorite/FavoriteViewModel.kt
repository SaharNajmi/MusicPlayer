package com.example.musicplayer.view.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player
import kotlinx.coroutines.launch

class FavoriteViewModel(val player: Player, private val musicRepository: MusicRepository) :
    ViewModel() {

    fun getFavorites(): LiveData<List<Song>> = liveData {
        val result = musicRepository.getFavorites()
        player.updateList(result)
        emit(result)
    }

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)

    fun insert(song: Song) {
        viewModelScope.launch {
            song.favorite = true
            musicRepository.update(song)
        }
    }

    fun delete(song: Song) {
        viewModelScope.launch {
            song.favorite = false
            musicRepository.update(song)
        }
    }
}