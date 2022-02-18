package com.example.musicplayer.data.db.dao

import androidx.room.*
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song

@Dao
interface MusicDao {
    @Query("Select * from song")
    fun getMusics(): List<Song>

    @Query("Select * from song where favorite = 1")
    fun getFavorites(): List<Song>

    @Query("Select * from artist")
    fun getArtists(): List<Artist>

    @Query("Select * from album")
    fun getAlbums(): List<Album>

    @Query("SELECT DISTINCT folderName from song")
    fun getFileNames(): List<String>

    @Query("Select * from song where artistID=:artistId")
    fun getArtistById(artistId: Long): List<Song>

    @Query("Select * from song where albumID=:albumId ")
    fun getAlbumById(albumId: Long): List<Song>

    @Query("Select * from song where folderName=:folderName ")
    fun getSongByFolderName(folderName: String): List<Song>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSongs(song: List<Song>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArtists(artist: List<Artist>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlbums(album: List<Album>)

    @Update
    fun update(song: Song)
}