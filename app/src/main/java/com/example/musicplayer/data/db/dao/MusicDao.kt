package com.example.musicplayer.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.musicplayer.data.model.SongModel

@Dao
interface MusicDao {
    @Query("Select * from music")
    fun getAll(): LiveData<List<SongModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(songModel: List<SongModel>)

    @Update
    fun update(songModel: SongModel)
}
