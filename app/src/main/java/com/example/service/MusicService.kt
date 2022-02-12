package com.example.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import com.example.model.SongModel
import com.example.myInterface.OnSongComplete

class MusicService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    lateinit var player: MediaPlayer
    lateinit var songModel: SongModel
    val musicBind: IBinder = MusicBinder()
    lateinit var onSongComplete: OnSongComplete

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        //  Player.player = player
        //   Player.liveDataPlayerState.value = Player.PAUSED
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
        try {
            if (mp != null) {
                mp.start()
                //Player.updateProgress(mp.duration)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun songComplete(onSongComplete: OnSongComplete) {
        this.onSongComplete = onSongComplete
    }

    override fun onCompletion(p0: MediaPlayer?) {
        onSongComplete.onSongComplete()
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        return false
    }
}