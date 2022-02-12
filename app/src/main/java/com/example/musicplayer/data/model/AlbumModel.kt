package com.example.musicplayer.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AlbumModel(
    val id: Long,
    val albumName: String,
    val artist: String,
    val albumImage: String
) : Parcelable