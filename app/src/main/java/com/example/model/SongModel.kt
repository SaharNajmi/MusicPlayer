package com.example.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "music")
@Parcelize
data class SongModel(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val artist: String,
    val albumID: Long,
    val artistID: Long,
    val songTitle: String,
    val coverImage: String,
    var lyrics: String = "",
    val folderName: String,
    val path: String,
    val duration: Int,
    val favorite: Boolean = false
) : Parcelable