package com.example.musicplayer.view.main

import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.SongModel

class MusicViewModel(val musicRepository: MusicRepository) : ViewModel() {
    val musics = musicRepository.musics

    fun insert(songModel: List<SongModel>) = musicRepository.insert(songModel)

    fun update(songModel: SongModel) = musicRepository.update(songModel)
}
