package com.example.musicplayer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.data.model.SongModel

@Dao
interface LyricsDao {
    @Query("Select * from music")
    fun getAll(): List<SongModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songModel: SongModel)
}
