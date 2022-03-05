package com.example.musicplayer.data.db.dao

import androidx.room.*
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song

@Dao
interface MusicDao {
    @Query("Select * from song")
    suspend fun getMusics(): List<Song>

    @Query("Select * from song where favorite = 1")
    suspend fun getFavorites(): List<Song>

    @Query("Select * from artist")
    suspend fun getArtists(): List<Artist>

    @Query("Select * from album")
    suspend fun getAlbums(): List<Album>

    @Query("SELECT DISTINCT folderName from song")
    suspend fun getFileNames(): List<String>

    @Query("Select * from song where artistID=:artistId")
    suspend fun getArtistById(artistId: Long): List<Song>

    @Query("Select * from song where albumID=:albumId ")
    suspend fun getAlbumById(albumId: Long): List<Song>

    @Query("Select * from song where folderName=:folderName ")
    suspend fun getSongByFolderName(folderName: String): List<Song>

    @Query("SELECT EXISTS (SELECT 1 FROM song)")
    suspend fun databaseExists(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(song: List<Song>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artist: List<Artist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(album: List<Album>)

    @Update
    suspend fun update(song: Song)
}