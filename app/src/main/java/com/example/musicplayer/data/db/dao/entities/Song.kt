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
    val title: String,
    val albumID: Long,
    val album: String,
    val artistID: Long,
    val artist: String,
    val folderID: Long,
    val folderName: String,
    val path: String,
    var duration: Int,
    val coverImage: String,
    var lyrics: String = "",
    var isLyrics: Boolean = false,
    var favorite: Boolean = false
) : Parcelable