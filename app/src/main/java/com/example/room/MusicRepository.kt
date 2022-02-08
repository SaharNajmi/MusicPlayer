package com.example.room

import com.example.model.SongModel

class MusicRepository(val dao: MusicDao) {
    val musics = dao.getAll()

    fun insert(songModel: List<SongModel>) = dao.insert(songModel)

    fun update(songModel: SongModel) = dao.update(songModel)
}
