package com.example.musicplayer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicplayer.data.db.dao.FavoriteDao
import com.example.musicplayer.data.model.SongModel


@Database(entities = [SongModel::class], version = 3, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private var INSTANCE: FavoriteDatabase? = null
        val DB_NAME = "favorite_database"

        fun getInstance(context: Context): FavoriteDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java,
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