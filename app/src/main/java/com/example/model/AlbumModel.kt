package com.example.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AlbumModel(
    val id: Long,
    val albumName: String,
    val artist: String,
    val albumImage: Uri
) : Parcelable