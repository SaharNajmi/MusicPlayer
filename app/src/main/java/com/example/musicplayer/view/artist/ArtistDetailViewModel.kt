package com.example.musicplayer.view.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player

class ArtistDetailViewModel(
    val player: Player,
    val musicRepository: MusicRepository
) : ViewModel() {

    fun getMusics(id: Long): LiveData<List<Song>> = liveData {
        val result = musicRepository.getArtistById(id)
        player.updateList(result)
        emit(result)
    }

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)
}