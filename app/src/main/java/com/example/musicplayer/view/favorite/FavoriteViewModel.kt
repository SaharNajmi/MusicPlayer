package com.example.musicplayer.view.favorite

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player

class FavoriteViewModel(val player: Player, private val musicRepository: MusicRepository) :
    ViewModel() {

    fun updateList() = player.updateList(getFavorites() as ArrayList<Song>)

    fun getFavorites(): List<Song> = musicRepository.getFavorites()

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)

    fun insert(song: Song) {
        song.favorite = true
        musicRepository.update(song)
    }

    fun delete(song: Song) {
        song.favorite = false
        musicRepository.update(song)
    }
}