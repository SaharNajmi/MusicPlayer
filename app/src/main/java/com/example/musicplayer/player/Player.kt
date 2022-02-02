package com.example.musicplayer.player

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.example.model.SongModel
import com.example.musicplayer.Repository
import java.io.IOException
import kotlin.random.Random

class Player private constructor(val mediaPlayer: MediaPlayer, val repository: Repository) :
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    var playerState = MutableLiveData<PlayerState>()
    var songModel = MutableLiveData<SongModel>()
    var progress = MutableLiveData<Int>()
    var musics = ArrayList<SongModel>()
    var isShuffle = false
    var isRepeat = false
    var duration = 0
    var songPosition = 0

    companion object {
        var INSTANCE: Player? = null
        fun getInstance(): Player {
            if (INSTANCE == null)
                INSTANCE = Player(MediaPlayer(), Repository())
            return INSTANCE!!
        }
    }

    fun initMusicPlayer() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnCompletionListener(this)
    }

    fun getSongs(context: Context): ArrayList<SongModel> {
        musics = repository.readExternalData(context)
        return repository.readExternalData(context)
    }

    fun songSelected(song: SongModel, posSong: Int) {
        songModel.value = song
        songPosition = posSong
        playSong()
        playerState.value = PlayerState.PLAYING
    }

    fun toggleState() {
        if (playerState.value == PlayerState.PAUSED)
            resumeSong()
        else if (playerState.value == PlayerState.PLAYING)
            pauseSong()
    }

    fun progressRunner(): Runnable {
        val progressRunner: Runnable = object : Runnable {
            override fun run() {
                progress.value = mediaPlayer.currentPosition
                //run again after 1 second
                postDelayed()
            }
        }
        return progressRunner
    }

    fun postDelayed() = Handler(Looper.getMainLooper()).postDelayed(progressRunner(), 1000)

    fun playerSeekTo(newSeekTo: Int) = mediaPlayer.seekTo(newSeekTo)

    fun playSong() {
        initMusicPlayer()
        mediaPlayer.reset()

        playerState.value = PlayerState.PLAYING

        try {
            mediaPlayer.setDataSource(musics[songPosition].path)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Run Progressbar seekBar
        progressRunner().run()

        mediaPlayer.prepareAsync()
    }


    fun pauseSong() {
        playerState.value = PlayerState.PAUSED
        mediaPlayer.pause()
    }

    fun resumeSong() {
        playerState.value = PlayerState.PLAYING
        mediaPlayer.start()

        //Run Progressbar seekBar
        progressRunner().run()
    }

    fun nextSong() {
        if (isShuffle) {
            shuffleSong()
        } else {
            songPosition++
            if (songPosition >= musics.size) songPosition = 0
            playSong()
            songModel.value = musics[songPosition]
        }
    }

    fun backSong() {
        if (isShuffle) {
            shuffleSong()
        } else {
            songPosition--
            if (songPosition < 0) songPosition = musics.size - 1
            playSong()
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
            mediaPlayer.start()
            duration = mediaPlayer.duration
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
            else -> nextSong()
        }
    }
}