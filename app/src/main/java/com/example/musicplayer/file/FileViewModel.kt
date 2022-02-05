package com.example.musicplayer.file

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.musicplayer.LocalMusic
import com.example.musicplayer.player.Player

class FileViewModel(val player: Player, val localMusic: LocalMusic) : ViewModel() {

    fun getFiles(context: Context): List<String> {
        val musics = player.getSongs(context)
        return localMusic.getFolderNames(musics)
    }
}