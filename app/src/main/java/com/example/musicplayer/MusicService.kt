package com.example.musicplayer

import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.widget.SeekBar
import com.model.SongModel
import java.io.IOException

class MusicService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    lateinit var player: MediaPlayer
    lateinit var songs: SongModel
    var playerState = PAUSED
    val musicBind: IBinder = MusicBinder()
    lateinit var seekBar: SeekBar

    companion object {
        val PAUSED = 1
        val PLAYING = 2
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        initMusicPlayer()
    }

    fun initMusicPlayer() {
        player.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player.setOnPreparedListener(this)
        player.setOnErrorListener(this)
        player.setOnCompletionListener(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return musicBind
    }

    inner class MusicBinder : Binder() {
        fun getService() = this@MusicService
    }

    override fun onUnbind(intent: Intent?): Boolean {
        player.stop()
        player.reset()
        player.release()
        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp!!.start()
        val duration = mp.duration
        seekBar.max = duration
        seekBar.postDelayed(progressRunner, 1000)

        //disable seekBar drag
        seekBar.setOnTouchListener { _, _ -> true }
    }

    fun playSong() {
        playerState = PLAYING
        player.reset()
        val playSong: SongModel = songs
        val currentSongId: Long = playSong.id

        val trackUri =
            ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSongId
            )

        try {
            player.setDataSource(applicationContext, trackUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        player.prepareAsync()

        //Run Progressbar seekBar
        progressRunner.run()
    }

    fun setSeekbar(seekBar: SeekBar) {
        this.seekBar = seekBar
    }

    val progressRunner: Runnable = object : Runnable {
        override fun run() {
            seekBar.progress = player.currentPosition
            if (player.isPlaying)
                seekBar.postDelayed(this, 1000)
        }
    }

    fun pauseSong() {
        playerState = PAUSED
        player.pause()
    }

    fun resumeSong() {
        playerState = PLAYING
        player.start()
    }

    fun setSong(songModel: SongModel) {
        songs = songModel
        playerState = PLAYING
        playSong()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        return false
    }
}