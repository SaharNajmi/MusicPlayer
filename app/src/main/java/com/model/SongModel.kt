package com.model

import android.net.Uri

data class SongModel(
    val id: Long,
    val artist: String,
    val songTitle: String,
    val coverImage: Uri,
    val lyrics: String = "",
    val favorite: Boolean = false
)