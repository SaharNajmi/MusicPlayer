package com.example.musicplayer.data.repository

import android.content.Context
import com.example.musicplayer.data.db.FavoriteDatabase
import com.example.musicplayer.data.db.LyricsDatabase
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.view.favorite.FavoriteRepository
import com.example.musicplayer.view.search.LyricsRepository

class LocalMusicRepository(val localMusic: LocalMusic, context: Context) {
    var favoriteRepository: FavoriteRepository
    var lyricsRepository: LyricsRepository

    init {
        val favDao = FavoriteDatabase.getInstance(context).favoriteDao()
        val musicDao = LyricsDatabase.getInstance(context).lyricsDao()

        favoriteRepository = FavoriteRepository(favDao)
        lyricsRepository = LyricsRepository(musicDao)
    }

    fun getMusics(context: Context): ArrayList<SongModel> {
        val musics = localMusic.readExternalData(context)
        val newList = ArrayList<SongModel>()
        musics.forEach { song ->
            favoriteRepository.getAll().forEach { favorite ->
                if (favorite.id == song.id)
                    song.favorite = true
            }
            lyricsRepository.getAll().forEach { lyrics ->
                if (lyrics.id == song.id) {
                    song.lyrics = lyrics.lyrics
                    song.isLyrics = true
                }
            }
            newList.add(song)
        }
        return newList
    }
}