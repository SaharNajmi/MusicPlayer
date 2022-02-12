package com.example.musicplayer.view.file

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.player.Player

class FileDetailViewModel(val player: Player) : ViewModel() {
    fun getMusics(context: Context) = player.getSongs(context)

    fun getMusicsInsideFolder(
        folderName: String,
        musics: ArrayList<SongModel>
    ): ArrayList<SongModel> {
        val newList = ArrayList<SongModel>()
        musics.forEach {
            if (folderName == it.folderName)
                newList.add(it)
        }
        player.musics = newList
        return newList
    }
}