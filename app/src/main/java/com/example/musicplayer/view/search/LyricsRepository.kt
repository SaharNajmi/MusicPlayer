package com.example.musicplayer.view.search

import com.example.musicplayer.data.db.dao.LyricsDao
import com.example.musicplayer.data.model.SongModel

class LyricsRepository(val dao: LyricsDao) {

    fun getAll() = dao.getAll()

    fun insert(songModel: SongModel) = dao.insert(songModel)
}
