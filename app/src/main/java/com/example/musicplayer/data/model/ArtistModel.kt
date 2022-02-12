package com.example.musicplayer.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistModel(
    val id: Long,
    val artist: String
) : Parcelable