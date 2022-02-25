package com.example.musicplayer.view.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player
import kotlinx.coroutines.Dispatchers

class HomeViewModel(val player: Player, private val musicRepository: MusicRepository) :
    ViewModel() {
    val musics = liveData(Dispatchers.IO) {
        val result = musicRepository.getMusics()
        player.updateList(result)
        emit(result)
    }

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)
}