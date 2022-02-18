package com.example.musicplayer.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.view.album.AlbumViewModel
import com.example.musicplayer.view.artist.ArtistViewModel
import com.example.musicplayer.view.file.FileViewModel
import com.example.musicplayer.view.search.LyricsViewModel

class MainViewModelFactory(val musicRepository: MusicRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AlbumViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(musicRepository) as T
            }
            modelClass.isAssignableFrom(ArtistViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ArtistViewModel(musicRepository) as T
            }
            modelClass.isAssignableFrom(FileViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return FileViewModel(musicRepository) as T
            }
            modelClass.isAssignableFrom(LyricsViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return LyricsViewModel(musicRepository) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}