package com.model

data class SongModel(
    val id: Long,
    val artist: String,
    val songTitle: String,
    val coverImage: String = "",
    val lyrics: String = "",
    val favorite: Boolean = false
)