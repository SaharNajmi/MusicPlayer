package com.example.musicplayer.view.favorite

import com.example.musicplayer.data.db.dao.FavoriteDao
import com.example.musicplayer.data.model.SongModel

class FavoriteRepository(val dao: FavoriteDao) {
    fun getAll() = dao.getAll()

    fun insert(songModel: SongModel) = dao.insert(songModel)

    fun delete(songModel: SongModel) = dao.delete(songModel)
}