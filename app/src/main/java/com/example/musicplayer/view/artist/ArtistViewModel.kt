package com.example.musicplayer.view.artist

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.repository.LocalMusic
import com.example.musicplayer.player.Player

class ArtistViewModel(val player: Player, val localMusic: LocalMusic) : ViewModel() {

    fun getArtists(context: Context): ArrayList<ArtistModel> {
        val musics = player.getSongs(context)
        val artistIDs = localMusic.getArtistIDs(musics)
        return localMusic.getArtists(musics, artistIDs)
    }
}