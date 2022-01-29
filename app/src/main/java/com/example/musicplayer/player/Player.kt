package com.example.musicplayer.player

import android.content.ContentUris
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.example.model.SongModel
import com.example.musicplayer.ReadExternalMusic
import java.io.IOException
import kotlin.random.Random

class Player : MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    var mp: MediaPlayer? = null
    var playerState = MutableLiveData<PlayerState>()
    var songModel = MutableLiveData<SongModel>()
    var progress = MutableLiveData<Int>()
    var musics = ArrayList<SongModel>()
    var isShuffle = false
    var isRepeat = false
    var duration = 0
    var songPosition = 0

    private object HOLDER {
        val INTANCE = Player()
    }

    companion object {
        fun getInstance(): Player {
            val instance: Player by lazy { HOLDER.INTANCE }
            return instance
        }
    }

    fun initMusicPlayer() {
        mp?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mp?.setOnPreparedListener(this)
        mp?.setOnErrorListener(this)
        mp?.setOnCompletionListener(this)
    }

    fun getSongs(context: Context): ArrayList<SongModel> {
        musics = ReadExternalMusic().readExternalData(context)
        return ReadExternalMusic().readExternalData(context)
    }

    fun songSelected(song: SongModel, posSong: Int) {
        songModel.value = song
        songPosition = posSong
        playerState.value = PlayerState.PLAYING
    }

    fun progressRunner(): Runnable {
        val progressRunner: Runnable = object : Runnable {
            override fun run() {
                if (mp != null)
                    progress.value = mp!!.currentPosition
            }
        }
        return progressRunner
    }

    fun playerSeekTo(newSeekTo: Int) {
        mp?.seekTo(newSeekTo)
    }

    fun playSong(index: Long, context: Context) {
        if (mp == null) {
            mp = MediaPlayer()
            initMusicPlayer()
        } else {
            mp!!.reset()
        }

        playerState.value = PlayerState.PLAYING

        val trackUri =
            ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                index
            )

        try {
            mp?.setDataSource(context, trackUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Run Progressbar seekBar
        progressRunner().run()

        mp?.prepareAsync()
    }


    fun pauseSong() {
        if (mp != null) {
            playerState.value = PlayerState.PAUSED
            mp?.pause()
        }
    }

    fun resumeSong() {
        if (mp != null) {
            playerState.value = PlayerState.PLAYING
            mp?.start()

            //Run Progressbar seekBar
            progressRunner().run()
        }
    }

    fun nextSong(context: Context) {
        if (isShuffle) {
            shuffleSong()
        } else {
            songPosition++
            if (songPosition >= musics.size) songPosition = 0
            playSong(musics[songPosition].id, context)

            songModel.value = musics[songPosition]
        }
    }

    fun backSong(context: Context) {
        if (isShuffle) {
            shuffleSong()
        } else {
            songPosition--
            if (songPosition < 0) songPosition = musics.size - 1
            playSong(musics[songPosition].id, context)
            songModel.value = musics[songPosition]
        }
    }

    fun repeatSong() {
        songModel.value = musics[songPosition]
    }

    fun shuffleSong() {
        var newSong = songPosition
        while (newSong == songPosition)
            newSong = Random.nextInt(musics.size - 1)
        songPosition = newSong
        songModel.value = musics[songPosition]
    }

    override fun onPrepared(p0: MediaPlayer?) {
        try {
            if (mp != null) {
                mp!!.start()
                duration = mp?.duration!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        return false
    }

    override fun onCompletion(p0: MediaPlayer?) {
        when {
            //repeat is on
            isRepeat -> repeatSong()

            //shuffle is on
            isShuffle -> shuffleSong()

            //next song
            // else -> nextSong(context)
        }
    }
}