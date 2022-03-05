package com.example.musicplayer.utils

import android.annotation.SuppressLint
import android.provider.MediaStore

object Constants {
    const val NEXT_ACTION = "NEXT"
    const val PLAY_ACTION = "PLAY"
    const val STOPFOREGROUND_ACTION = "STOP"
    const val PERMISSION_CODE = 1
    const val BASE_URL = "https://private-anon-75d190a1d2-lyricsovh.apiary-proxy.com/v1/"

    @SuppressLint("InlinedApi")
    val baseProjection = arrayOf(
        /*0*/  MediaStore.Audio.Media._ID,
        /*1*/  MediaStore.Audio.Media.TITLE,
        /*2*/  MediaStore.Audio.Media.ALBUM_ID,
        /*3*/  MediaStore.Audio.Media.ALBUM,
        /*4*/ MediaStore.Audio.Media.ARTIST_ID,
        /*5*/ MediaStore.Audio.Media.ARTIST,
        /*6*/ MediaStore.Audio.Media.DATA,
        /*7*/ MediaStore.Audio.Media.DURATION
        //MediaStore.Audio.Media.BUCKET_ID,
        //MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
    )
}