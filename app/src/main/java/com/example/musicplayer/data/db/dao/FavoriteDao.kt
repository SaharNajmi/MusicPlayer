package com.example.musicplayer.data.db.dao

import androidx.room.*
import com.example.musicplayer.data.model.SongModel

@Dao
interface FavoriteDao {
    @Query("Select * from music")
    fun getAll(): List<SongModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songModel: SongModel)

    @Delete
    fun delete(songModel: SongModel)
}