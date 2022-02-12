package com.example.myInterface

import com.example.model.SongModel

interface SongEventListener {
    fun onSelect(songModel: SongModel, posSong: Int)
}