package com.example.myInterface

import com.example.model.ArtistModel

interface ArtistEventListener {
    fun onSelect(artistModel: ArtistModel)
}