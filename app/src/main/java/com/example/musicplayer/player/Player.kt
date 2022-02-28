package com.example.musicplayer.player

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.utils.PlayerState
import java.io.IOException
import kotlin.random.Random

class Player() :
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    val mediaPlayer: MediaPlayer = MediaPlayer()
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState
    val song = MutableLiveData<Song>()
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress
    private var musics = listOf<Song>()
    var isShuffle = false
        set(value) {
            if (value)
                isRepeat = false
            field = value
        }
    var isRepeat = false
        set(value) {
            if (value)
                isShuffle = false
            field = value
        }
    var duration = 0
        private set
    var songPosition = 0

    init {
        _progress.value = 0
        _playerState.value = PlayerState.PAUSED
        initMusicPlayer()
    }

    fun updateList(songs: List<Song>) {
        musics = songs
    }

    fun initMusicPlayer() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnCompletionListener(this)
    }

    fun songSelected(song: Song, posSong: Int) {
        this.song.value = song
        songPosition = posSong
        playSong()
        _playerState.value = PlayerState.PLAYING
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
                _progress.value = mediaPlayer.currentPosition
                //run again after 1 second
                // if (mediaPlayer.isPlaying)
                if (playerState.value == PlayerState.PLAYING)
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

        _playerState.value = PlayerState.PLAYING

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
        _playerState.value = PlayerState.PAUSED
        mediaPlayer.pause()
    }

    fun resumeSong() {
        _playerState.value = PlayerState.PLAYING
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
            song.value = musics[songPosition]
        }
    }

    fun backSong() {
        if (isShuffle) {
            shuffleSong()
        } else {
            songPosition--
            if (songPosition < 0) songPosition = musics.size - 1
            playSong()
            song.value = musics[songPosition]
        }
    }

    fun repeatSong() {
        song.value = musics[songPosition]
        playSong()
    }

    fun shuffleSong() {
        if (musics.size != 1) {
            var newSong = songPosition
            while (newSong == songPosition)
                newSong = Random.nextInt(musics.size - 1)
            songPosition = newSong
        }
        song.value = musics[songPosition]
        playSong()
    }

    override fun onPrepared(p0: MediaPlayer?) {
        try {
            mediaPlayer.start()
            duration = musics[songPosition].duration
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