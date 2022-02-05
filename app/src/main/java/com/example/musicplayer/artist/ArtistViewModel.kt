package com.example.musicplayer.artist

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.model.ArtistModel
import com.example.musicplayer.LocalMusic
import com.example.musicplayer.player.Player

class ArtistViewModel(val player: Player, val localMusic: LocalMusic) : ViewModel() {

    fun getArtists(context: Context): ArrayList<ArtistModel> {
        val musics = player.getSongs(context)
        val artistIDs = localMusic.getArtistIDs(musics)
        return localMusic.getArtists(musics, artistIDs)
    }
}