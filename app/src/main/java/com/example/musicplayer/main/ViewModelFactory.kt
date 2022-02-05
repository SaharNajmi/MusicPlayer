package com.example.musicplayer.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.LocalMusic
import com.example.musicplayer.album.AlbumDetailViewModel
import com.example.musicplayer.album.AlbumViewModel
import com.example.musicplayer.all.HomeViewModel
import com.example.musicplayer.artist.ArtistDetailViewModel
import com.example.musicplayer.artist.ArtistViewModel
import com.example.musicplayer.detail.DetailViewModel
import com.example.musicplayer.file.FileDetailViewModel
import com.example.musicplayer.file.FileViewModel
import com.example.musicplayer.player.Player
import com.example.musicplayer.search.SearchMusicViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val instance = Player.getInstance()
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(instance) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(instance) as T
            }
            modelClass.isAssignableFrom(AlbumDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(instance) as T
            }
            modelClass.isAssignableFrom(ArtistDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ArtistDetailViewModel(instance) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(instance) as T
            }
            modelClass.isAssignableFrom(FileDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return FileDetailViewModel(instance) as T
            }
            modelClass.isAssignableFrom(AlbumViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(instance, LocalMusic()) as T
            }
            modelClass.isAssignableFrom(ArtistViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ArtistViewModel(instance, LocalMusic()) as T
            }
            modelClass.isAssignableFrom(FileViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return FileViewModel(instance, LocalMusic()) as T
            }
            modelClass.isAssignableFrom(SearchMusicViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return SearchMusicViewModel(instance) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}