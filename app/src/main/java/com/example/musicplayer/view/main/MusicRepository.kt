package com.example.musicplayer.view.main

import com.example.musicplayer.data.db.dao.MusicDao
import com.example.musicplayer.data.model.SongModel

class MusicRepository(val dao: MusicDao) {
    val musics = dao.getAll()

    fun insert(songModel: List<SongModel>) = dao.insert(songModel)

    fun update(songModel: SongModel) = dao.update(songModel)
}
