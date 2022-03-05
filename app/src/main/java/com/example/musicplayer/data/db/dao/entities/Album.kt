package com.example.musicplayer.data.db.dao.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "album")
class Album(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val albumName: String,
    val artist: String,
    val albumImage: String
) : Parcelable