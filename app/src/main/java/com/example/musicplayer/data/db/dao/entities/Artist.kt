package com.example.musicplayer.data.db.dao.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "artist")
class Artist(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val artist: String
) : Parcelable