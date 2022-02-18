package com.example.musicplayer.view.all

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player

class HomeViewModel(val player: Player, private val musicRepository: MusicRepository) :
    ViewModel() {

    fun updateList() = player.updateList(getMusics() as ArrayList<Song>)

    fun getMusics() = musicRepository.getMusics()

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)
}