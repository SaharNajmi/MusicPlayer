package com.example.musicplayer.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.musicplayer.data.db.dao.entities.Album
import com.example.musicplayer.data.db.dao.entities.Artist
import com.example.musicplayer.data.db.dao.entities.Song
import java.io.File

class LocalMusic(context: Context) {
    var musicResolver: ContentResolver = context.contentResolver
    val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val musicCursor = musicResolver.query(musicUri, null, null, null, null)

    @SuppressLint("Recycle", "InlinedApi")
    fun musics(): ArrayList<Song> {
        val musics = ArrayList<Song>()
        if (musicCursor != null && musicCursor.moveToFirst()) {
            val songId = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val artistId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
            val path = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val duration = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val folderName: String = File(musicCursor.getString(path)).parentFile.name
            //val folderName = musicCursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)
            //val folderId = musicCursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID)

            while (musicCursor.moveToNext()) {
                //get cover image song
                val image_uri = Uri.parse("content://media/external/audio/albumart")
                val album_uri =
                    ContentUris.withAppendedId(image_uri, musicCursor.getLong(albumId))

                //add music into array song
                musics.add(
                    Song(
                        musicCursor.getLong(songId),
                        musicCursor.getString(songArtist),
                        musicCursor.getLong(albumId),
                        musicCursor.getLong(artistId),
                        musicCursor.getString(songTitle),
                        album_uri.toString(),
                        folderName = folderName,
                        path = musicCursor.getString(path),
                        duration = musicCursor.getInt(duration)
                    )
                )

            }
        }
        return musics
    }

    fun artists(): ArrayList<Artist> {
        val artists = ArrayList<Artist>()
        if (musicCursor != null && musicCursor.moveToFirst()) {
            val artist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val artistId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)

            while (musicCursor.moveToNext()) {

                //add artists into array artist
                artists.add(
                    Artist(
                        musicCursor.getLong(artistId),
                        musicCursor.getString(artist),
                    )
                )

            }
        }
        return artists
    }

    fun albums(): ArrayList<Album> {
        val albums = ArrayList<Album>()
        if (musicCursor != null && musicCursor.moveToFirst()) {
            val artist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumName = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

            while (musicCursor.moveToNext()) {
                //get cover image song
                val image_uri = Uri.parse("content://media/external/audio/albumart")
                val album_uri =
                    ContentUris.withAppendedId(image_uri, musicCursor.getLong(albumId))

                //add albums into array album
                albums.add(
                    Album(
                        musicCursor.getLong(albumId),
                        musicCursor.getString(albumName),
                        musicCursor.getString(artist),
                        album_uri.toString()
                    )
                )
            }
        }
        return albums
    }
}