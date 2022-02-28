package com.example.musicplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicplayer.data.db.dao.MusicDao
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song

@Database(entities = [Song::class, Artist::class, Album::class], version = 1, exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}