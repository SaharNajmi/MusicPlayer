package com.example.musicplayer.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.album.AlbumDetailViewModel
import com.example.musicplayer.view.album.AlbumViewModel
import com.example.musicplayer.view.all.HomeViewModel
import com.example.musicplayer.view.artist.ArtistDetailViewModel
import com.example.musicplayer.view.artist.ArtistViewModel
import com.example.musicplayer.view.detail.DetailViewModel
import com.example.musicplayer.view.file.FileDetailViewModel
import com.example.musicplayer.view.file.FileViewModel
import com.example.musicplayer.view.main.MainViewModel
import com.example.musicplayer.view.search.LyricsViewModel
import com.example.musicplayer.view.search.SearchMusicViewModel

class BaseViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val instance = Player.getInstance(context)
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