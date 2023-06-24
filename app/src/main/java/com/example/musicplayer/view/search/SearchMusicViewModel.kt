package com.example.musicplayer.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.repository.MusicRepository
import com.example.musicplayer.player.Player
import com.example.musicplayer.view.all.SongAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class SearchMusicViewModel(val player: Player, val musicRepository: MusicRepository) : ViewModel() {
    fun songSelected(song: Song, posSong: Int) = player.songSelected(song, posSong)
    fun searchSong(value: String, adapter: SongAdapter) {
        val filterSongs = ArrayList<Song>()
        viewModelScope.launch(Dispatchers.IO) {
            for (song in musicRepository.getMusics()) {
                var isListAdded = false
                if (song.title.toLowerCase().contains(value.toLowerCase())
                    && value != ""
                ) {
                    filterSongs.add(song)
                    isListAdded = true
                }
                if (song.artist.toLowerCase().contains(value.toLowerCase())
                    && value != ""
                ) {
                    if (!isListAdded)
                        filterSongs.add(song)
                }
            }
        }
        //update list musics
        updateList(filterSongs)

        //update adapter
        adapter.updateList(filterSongs)
    }

    fun updateList(songs: ArrayList<Song>) = player.updateList(songs)
}