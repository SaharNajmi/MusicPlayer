package com.example.musicplayer.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.album.AlbumDetailViewModel
import com.example.musicplayer.view.all.HomeViewModel
import com.example.musicplayer.view.artist.ArtistDetailViewModel
import com.example.musicplayer.view.favorite.FavoriteViewModel
import com.example.musicplayer.view.file.FileDetailViewModel
import com.example.musicplayer.view.main.MainViewModel
import com.example.musicplayer.view.search.SearchMusicViewModel

class BaseViewModelFactory(val player: Player, val musicRepository: MusicRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AlbumDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(player, musicRepository) as T
            }
            modelClass.isAssignableFrom(ArtistDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ArtistDetailViewModel(player, musicRepository) as T
            }
            modelClass.isAssignableFrom(FileDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return FileDetailViewModel(player, musicRepository) as T
            }
            modelClass.isAssignableFrom(SearchMusicViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return SearchMusicViewModel(player, musicRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(player, musicRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(player, musicRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(player, musicRepository) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}