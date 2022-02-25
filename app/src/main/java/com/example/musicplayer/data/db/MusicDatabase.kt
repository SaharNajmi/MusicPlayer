package com.example.musicplayer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicplayer.data.db.dao.MusicDao
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song

@Database(entities = [Song::class, Artist::class, Album::class], version = 1, exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao

    companion object {
        private var INSTANCE: MusicDatabase? = null
        val DB_NAME = "music_database"

        fun getInstance(context: Context): MusicDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return INSTANCE!!
        }
    }
}