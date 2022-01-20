package com.example.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(
    val id: Long,
    val artist: String,
    val albumID: Long,
    val songTitle: String,
    val coverImage: Uri,
    val lyrics: String = "",
    val favorite: Boolean = false
) : Parcelable