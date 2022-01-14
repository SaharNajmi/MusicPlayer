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
import android.widget.TextView
import com.example.`interface`.OnSongComplete
import com.model.SongModel
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MusicService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    lateinit var player: MediaPlayer
    lateinit var songModel: SongModel
    var playerState = PAUSED
    val musicBind: IBinder = MusicBinder()
    lateinit var seekBar: SeekBar
    lateinit var seekbarDetail: SeekBar
    lateinit var startPoint: TextView
    lateinit var endPoint: TextView
    lateinit var onSongComplete: OnSongComplete
    var listMusic = ArrayList<SongModel>()
    var songPosition = 0

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
        seekbarDetail.max = duration
        seekbarDetail.postDelayed(progressRunner, 1000)

        //disable seekBar drag
        seekBar.setOnTouchListener { _, _ -> true }

        endPoint.text = String.format(
            "%d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration.toLong()))
        )
    }

    val progressRunner: Runnable = object : Runnable {
        override fun run() {
            seekBar.progress = player.currentPosition
            seekbarDetail.progress = player.currentPosition
            if (player.isPlaying) {
                seekBar.postDelayed(this, 1000)
                seekbarDetail.postDelayed(this, 1000)
            }
        }
    }

    fun playSong(index: Long) {
        playerState = PLAYING
        player.reset()
        val trackUri =
            ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                index
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

    fun setUI(seekBar: SeekBar, seekBarDetail: SeekBar, start: TextView, end: TextView) {
        this.seekbarDetail = seekBarDetail
        this.seekBar = seekBar
        startPoint = start
        endPoint = end
        seekBarDetail.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2)
                    player.seekTo(p1)

                //  seekBar.progress = p1

                startPoint.text =
                    String.format(
                        "%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(p1.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(p1.toLong()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(p1.toLong()))
                    )
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    fun getListSong(list: ArrayList<SongModel>) {
        listMusic = list
    }

    fun setPositionSong(pos: Int) {
        songPosition = pos
    }

    fun setSong(songModel: SongModel) {
        this.songModel = songModel
        playerState = PLAYING
        playSong(songModel.id)
    }

    fun pauseSong() {
        playerState = PAUSED
        player.pause()
        /*seekBar.removeCallbacks(progressRunner)
        seekbarDetail.removeCallbacks(progressRunner)*/
    }

    fun resumeSong() {
        playerState = PLAYING
        player.start()
        //progressSeekBar run again when resume Song
        progressRunner.run()
    }

    fun repeatSong() {
        playSong(listMusic[songPosition].id)
    }

    fun shuffleSong() {
        var newSong = songPosition
        while (newSong == songPosition)
            newSong = Random.nextInt(listMusic.size - 1)
        songPosition = newSong

        playSong(listMusic[newSong].id)
    }

    fun nextSong() {
        songPosition++
        if (songPosition >= listMusic.size) songPosition = 0
        playSong(listMusic[songPosition].id)
    }

    fun backSong() {
        songPosition--
        if (songPosition < 0) songPosition = listMusic.size - 1
        playSong(listMusic[songPosition].id)
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