package com.example.musicplayer.data.repository

import com.example.musicplayer.data.db.dao.MusicDao
import com.example.musicplayer.data.db.dao.entities.Song

class MusicRepository(private val localMusic: LocalMusic, val musicDao: MusicDao) {

    fun getFavorites() = musicDao.getFavorites()

    fun getMusics() = musicDao.getMusics()

    fun getArtists() = musicDao.getArtists()

    fun getAlbums() = musicDao.getAlbums()

    fun getFileNames() = musicDao.getFileNames()

    fun getArtistById(artistId: Long) = musicDao.getArtistById(artistId)

    fun getAlbumById(albumId: Long) = musicDao.getAlbumById(albumId)

    fun getSongByFolderName(folderName: String) = musicDao.getSongByFolderName(folderName)

    fun insertSongs() = musicDao.insertSongs(localMusic.musics())

    fun insertAlbums() = musicDao.insertAlbums(localMusic.albums())

    fun insertArtists() = musicDao.insertArtists(localMusic.artists())

    fun update(song: Song) = musicDao.update(song)
}