package com.example.musicplayer.data.repository

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LocalMusic(private val context: Context) {

    suspend fun getAllSongs(): List<Song> =
        withContext(Dispatchers.IO) { songs(makeSongCursor()) }

    suspend fun getAllArtists(): List<Artist> =
        withContext(Dispatchers.IO) { artists(makeSongCursor()) }

    suspend fun getAllAlbums(): List<Album> =
        withContext(Dispatchers.IO) { albums(makeSongCursor()) }

    private fun songs(cursor: Cursor?): List<Song> {
        val songs = arrayListOf<Song>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getSongFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    private fun artists(cursor: Cursor?): List<Artist> {
        val artists = arrayListOf<Artist>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                artists.add(getArtistFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return artists
    }

    private fun albums(cursor: Cursor?): List<Album> {
        val albums = arrayListOf<Album>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                albums.add(getAlbumFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return albums
    }

    private fun getSongFromCursorImpl(cursor: Cursor): Song {
        val id = cursor.getLong(0)
        val title = cursor.getString(1)
        val albumID = cursor.getLong(2)
        val album = cursor.getString(3)
        val artistID = cursor.getLong(4)
        val artist = cursor.getString(5)
        val path = cursor.getString(6)
        val duration = cursor.getInt(7)
        val folderID: Long = 0
        val folderName: String = File(path).parentFile.name
        val uri = Uri.parse("content://media/external/audio/albumart")
        val coverImage = ContentUris.withAppendedId(uri, albumID)

        return Song(
            id = id,
            title = title,
            albumID = albumID,
            album = album ?: "<unknown>",
            artistID = artistID,
            artist = artist ?: "<unknown>",
            folderID = folderID,
            folderName = folderName,
            path = path,
            duration = duration,
            coverImage = coverImage.toString()
        )
    }

    private fun getArtistFromCursorImpl(cursor: Cursor): Artist {
        val artistID = cursor.getLong(4)
        val artist = cursor.getString(5)

        return Artist(
            artistID,
            artist ?: "<unknown>",
        )
    }

    private fun getAlbumFromCursorImpl(cursor: Cursor): Album {
        val albumID = cursor.getLong(2)
        val album = cursor.getString(3)
        val artist = cursor.getString(5)
        val uri = Uri.parse("content://media/external/audio/albumart")
        val coverImage = ContentUris.withAppendedId(uri, albumID)

        return Album(
            id = albumID,
            albumName = album ?: "<unknown>",
            artist = artist ?: "<unknown>",
            coverImage.toString()
        )
    }

    @SuppressLint("Recycle")
    private fun makeSongCursor(): Cursor? {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        return try {
            context.applicationContext.contentResolver.query(
                uri,
                Constants.baseProjection,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
        } catch (e: SecurityException) {
            null
        }
    }
}

