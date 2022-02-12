package com.example.musicplayer.view.album

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.player.Player

class AlbumDetailViewModel(val player: Player) : ViewModel() {
    fun getAlbums(alumId: Long, context: Context): ArrayList<SongModel> {
        val newList = ArrayList<SongModel>()
        val musics = player.getSongs(context)
        musics.forEach {
            if (alumId == it.albumID)
                newList.add(it)
        }
        player.musics = newList
        return newList
    }

}