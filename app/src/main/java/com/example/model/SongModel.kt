package com.example.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(
    val id: Long,
    val artist: String,
    val albumID: Long,
    val artistID: Long,
    val songTitle: String,
    val coverImage: Uri,
    val lyrics: String = "",
    val folderName: String,
    val path: String,
    val duration: Int,
    val favorite: Boolean = false
) : Parcelable