package com.example.musicplayer.view.file

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player

class FileDetailViewModel(val player: Player, val musicRepository: MusicRepository) : ViewModel() {

    fun updateList(folderName: String) =
        player.updateList(getSongByFolderName(folderName) as ArrayList<Song>)

    fun getSongByFolderName(folderName: String) = musicRepository.getSongByFolderName(folderName)

    fun getMusics() = musicRepository.getMusics()

    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)
}