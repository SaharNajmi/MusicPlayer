package com.example.musicplayer.search

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.model.SongModel
import com.example.musicplayer.all.SongAdapter
import com.example.musicplayer.player.Player

class SearchMusicViewModel(val player: Player) : ViewModel() {
    fun getMusics(context: Context) = player.getSongs(context)

    fun searchSong(value: String, context: Context, adapter: SongAdapter) {
        val filterSongs = ArrayList<SongModel>()
        val musics = player.getSongs(context)
        for (song in musics) {
            var isListAdded = false
            if (song.songTitle.toLowerCase().contains(value.toLowerCase())) {
                filterSongs.add(song)
                isListAdded = true
            }
            if (song.artist.toLowerCase().contains(value.toLowerCase())) {
                if (!isListAdded)
                    filterSongs.add(song)
            }
        }
        player.musics = filterSongs
        adapter.updateList(filterSongs)
    }

}