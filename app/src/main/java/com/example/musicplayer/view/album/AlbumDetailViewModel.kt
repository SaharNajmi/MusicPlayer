package com.example.musicplayer.view.album

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player

class AlbumDetailViewModel(val player: Player, val musicRepository: MusicRepository) : ViewModel() {

    fun updateList(albumId: Long) = player.updateList(getAlbumById(albumId) as ArrayList<Song>)

    fun getAlbumById(alumId: Long) = musicRepository.getAlbumById(alumId)

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)
}