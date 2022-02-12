package com.example.musicplayer.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import java.io.File

class LocalMusic {
    val albums = ArrayList<AlbumModel>()
    val folderNames = ArrayList<String>()
    val albumIDs = ArrayList<Long>()
    val artists = ArrayList<ArtistModel>()
    val artistIDs = ArrayList<Long>()

    @SuppressLint("Recycle", "InlinedApi")
    fun readExternalData(context: Context): ArrayList<SongModel> {
        val musics = ArrayList<SongModel>()
        var musicResolver: ContentResolver = context.contentResolver
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor = musicResolver.query(musicUri, null, null, null, null)
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
                    SongModel(
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

    fun getFolderNames(list: ArrayList<SongModel>): List<String> {
        list.forEach {
            folderNames.add(it.folderName)
        }
        return folderNames.distinct()
    }

    fun getAlbumIDs(list: ArrayList<SongModel>): List<Long> {
        list.forEach {
            albumIDs.add(it.albumID)
        }
        return albumIDs.distinct()
    }

    fun getArtistIDs(list: ArrayList<SongModel>): List<Long> {
        list.forEach {
            artistIDs.add(it.artistID)
        }
        return artistIDs.distinct()
    }

    fun getAlbums(
        musics: ArrayList<SongModel>,
        albumIDs: List<Long>
    ): ArrayList<AlbumModel> {
        var j = 0
        var k = 0
        while (albumIDs.size > j) {
            while (musics.size > k) {
                if (musics[k].albumID == albumIDs[j]) {
                    albums.add(
                        AlbumModel(
                            musics[k].albumID,
                            musics[k].songTitle,
                            musics[k].artist,
                            musics[k].coverImage,
                        )
                    )

                    break
                }
                k++
            }
            j++
        }
        return albums
    }

    fun getArtists(
        music: ArrayList<SongModel>,
        artistIDs: List<Long>
    ): ArrayList<ArtistModel> {
        var j = 0
        var k = 0
        while (artistIDs.size > j) {
            while (music.size > k) {
                if (music[k].artistID == artistIDs[j]) {
                    artists.add(
                        ArtistModel(
                            music[k].artistID,
                            music[k].artist
                        )
                    )
                    break
                }
                k++
            }
            j++
        }
        return artists
    }
}