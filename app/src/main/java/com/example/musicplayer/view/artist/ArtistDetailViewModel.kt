package com.example.musicplayer.view.artist

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player

class ArtistDetailViewModel(val player: Player, val musicRepository: MusicRepository) :
    ViewModel() {

    fun updateList(artistId: Long) {
        player.updateList(getArtistById(artistId) as ArrayList<Song>)
    }

    fun getArtistById(artistId: Long) = musicRepository.getArtistById(artistId)

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)
}