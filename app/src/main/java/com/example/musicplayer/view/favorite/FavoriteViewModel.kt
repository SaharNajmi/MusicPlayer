package com.example.musicplayer.view.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.musicplayer.data.db.FavoriteDatabase
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.player.Player


class FavoriteViewModel(app: Application) : AndroidViewModel(app) {
    var repository: FavoriteRepository

    init {
        val musicDao = FavoriteDatabase.getInstance(getApplication()).favoriteDao()
        val player: Player = Player.getInstance(app)
        repository = FavoriteRepository(musicDao)
        player.musics = getAll()
    }

    fun getAll(): ArrayList<SongModel> = repository.getAll() as ArrayList<SongModel>

    fun insert(songModel: SongModel) {
        songModel.favorite = true
        repository.insert(songModel)
    }

    fun delete(songModel: SongModel) {
        songModel.favorite = false
        repository.delete(songModel)
    }
}