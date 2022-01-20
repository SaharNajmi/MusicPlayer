package com.example.musicplayer

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.model.AlbumModel
import com.example.model.SongModel

class ReadExternalDate {
    val listMusic = ArrayList<SongModel>()
    val listAlbum = ArrayList<AlbumModel>()
    val listAlbumId = ArrayList<Long>()

    fun readExternalData(context: Context): ArrayList<SongModel> {
        var musicResolver: ContentResolver = context.contentResolver
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor = musicResolver.query(musicUri, null, null, null, null)
        if (musicCursor != null && musicCursor.moveToFirst()) {
            val songId = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val artistId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)

            var listArtist = ArrayList<Long>()
            var listAlbum = ArrayList<Long>()
            while (musicCursor.moveToNext()) {
                //get cover image song
                val IMAGE_URI = Uri.parse("content://media/external/audio/albumart")
                val album_uri =
                    ContentUris.withAppendedId(IMAGE_URI, musicCursor.getLong(albumId))

                //add music into array song
                listMusic.add(
                    SongModel(
                        musicCursor.getLong(songId),
                        musicCursor.getString(songArtist),
                        musicCursor.getLong(albumId),
                        musicCursor.getString(songTitle),
                        album_uri
                    )
                )

                listArtist.add(musicCursor.getLong(artistId))
                listAlbum.add(musicCursor.getLong(albumId))

            }
        }
        return listMusic
    }

    fun getListAlbumId(list: ArrayList<SongModel>): List<Long> {
        list.forEach {
            listAlbumId.add(it.albumID)
        }
        return listAlbumId.distinct()
    }

    fun getListAlbum(
        listMusic: ArrayList<SongModel>,
        listAlbumId: List<Long>
    ): ArrayList<AlbumModel> {
        var j = 0
        var k = 0
        while (listAlbumId.size > j) {
            while (listMusic.size > k) {
                if (listMusic[k].albumID == listAlbumId[j]) {
                    listAlbum.add(
                        AlbumModel(
                            listMusic[k].albumID,
                            listMusic[k].songTitle,
                            listMusic[k].artist,
                            listMusic[k].coverImage,
                        )
                    )

                    break
                }
                k++
            }
            j++
        }
        return listAlbum
    }
}