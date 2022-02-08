package com.example.room

import androidx.lifecycle.ViewModel
import com.example.model.SongModel

class MusicViewModel(val musicRepository: MusicRepository) : ViewModel() {
    val musics = musicRepository.musics

    fun insert(songModel: List<SongModel>) = musicRepository.insert(songModel)

    fun update(songModel: SongModel) = musicRepository.update(songModel)
}
