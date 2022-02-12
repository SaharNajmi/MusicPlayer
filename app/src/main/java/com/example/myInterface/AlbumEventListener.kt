package com.example.myInterface

import com.example.model.AlbumModel

interface AlbumEventListener {
    fun onSelect(albumModel: AlbumModel)
}