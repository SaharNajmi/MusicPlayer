package com.example.musicplayer.data.db.dao.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "song")
data class Song(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val artist: String,
    val albumID: Long,
    val artistID: Long,
    val songTitle: String,
    val coverImage: String,
    var lyrics: String = "",
    var isLyrics: Boolean = false,
    val folderName: String,
    val path: String,
    val duration: Int,
    var favorite: Boolean = false
) : Parcelable