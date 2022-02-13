package com.example.musicplayer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicplayer.data.db.dao.LyricsDao
import com.example.musicplayer.data.model.SongModel

@Database(entities = [SongModel::class], version = 1, exportSchema = false)
abstract class LyricsDatabase : RoomDatabase() {
    abstract fun lyricsDao(): LyricsDao

    companion object {
        private var INSTANCE: LyricsDatabase? = null
        val DB_NAME = "music_data_database"

        fun getInstance(context: Context): LyricsDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    LyricsDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return INSTANCE!!
        }
    }
}
