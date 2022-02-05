package com.example.musicplayer.artist

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.model.SongModel
import com.example.musicplayer.player.Player

class ArtistDetailViewModel(val player: Player) : ViewModel() {

    fun getArtists(artistId: Long, context: Context): ArrayList<SongModel> {
        val newList = ArrayList<SongModel>()
        val musics = player.getSongs(context)
        musics.forEach {
            if (artistId == it.artistID)
                newList.add(it)
        }
        player.musics = newList
        return newList
    }

}