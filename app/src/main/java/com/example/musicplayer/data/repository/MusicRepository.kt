package com.example.musicplayer.data.repository

import com.example.musicplayer.data.db.dao.MusicDao
import com.example.musicplayer.data.db.dao.entities.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val localMusic: LocalMusic,
    private val musicDao: MusicDao
) {

    suspend fun getFavorites() = withContext(Dispatchers.IO) { musicDao.getFavorites() }

    suspend fun getMusics() = withContext(Dispatchers.IO) { musicDao.getMusics() }

    suspend fun getArtists() = withContext(Dispatchers.IO) { musicDao.getArtists() }

    suspend fun getAlbums() = withContext(Dispatchers.IO) { musicDao.getAlbums() }

    suspend fun getFileNames() = withContext(Dispatchers.IO) { musicDao.getFileNames() }

    suspend fun getArtistById(artistId: Long) =
        withContext(Dispatchers.IO) { musicDao.getArtistById(artistId) }

    suspend fun getAlbumById(albumId: Long) =
        withContext(Dispatchers.IO) { musicDao.getAlbumById(albumId) }

    suspend fun getSongByFolderName(folderName: String) = withContext(Dispatchers.IO) {
        musicDao.getSongByFolderName(folderName)
    }

    suspend fun insertMusics() = withContext(Dispatchers.IO) {
        musicDao.insertSongs(localMusic.musics())
    }

    suspend fun databaseExists() = withContext(Dispatchers.IO) { musicDao.databaseExists() }

    suspend fun insertAlbums() = withContext(Dispatchers.IO) {
        musicDao.insertAlbums(localMusic.albums())
    }

    suspend fun insertArtists() = withContext(Dispatchers.IO) {
        musicDao.insertArtists(localMusic.artists())
    }

    suspend fun update(song: Song) = withContext(Dispatchers.IO) {
        musicDao.update(song)
    }
}