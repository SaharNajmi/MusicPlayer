package com.example.musicplayer.album

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.model.AlbumModel
import com.example.musicplayer.LocalMusic
import com.example.musicplayer.player.Player

class AlbumViewModel(val player: Player, val localMusic: LocalMusic) : ViewModel() {

    fun getAlbums(context: Context): ArrayList<AlbumModel> {
        //get array list album
        val musics = player.getSongs(context)
        val albumIDs = localMusic.getAlbumIDs(musics)
        return localMusic.getAlbums(musics, albumIDs)
    }
}