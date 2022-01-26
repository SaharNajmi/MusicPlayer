package com.example.musicplayer.player

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import com.example.model.SongModel
import com.example.musicplayer.ReadExternalDate
import java.io.IOException
import kotlin.random.Random

@SuppressLint("StaticFieldLeak")
object Player {
    var liveDataSongModel = MutableLiveData<SongModel>()
    var liveDataPlayerState = MutableLiveData<Int>()
    var listMusic = ArrayList<SongModel>()
    var songPosition = 0
    lateinit var player: MediaPlayer
    lateinit var context: Context
    val PAUSED = 1
    val PLAYING = 2
    var isShuffle = false
    var isRepeat = false
    lateinit var seekBar: SeekBar

    fun getListSong(context: Context): ArrayList<SongModel> {
        return ReadExternalDate().readExternalData(context)
    }

    fun playSong(index: Long, context: Context) {
        liveDataPlayerState.value = PLAYING
        player.reset()
        val trackUri =
            ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                index
            )
        try {
            player.setDataSource(context, trackUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Run Progressbar seekBar
        progressRunner(player).run()

        player.prepareAsync()
    }

    fun setSong(songModel: SongModel, posSong: Int) {
        this.liveDataSongModel.value = songModel
        this.songPosition = posSong
        liveDataPlayerState.value = PLAYING
    }

    fun pauseSong() {
        liveDataPlayerState.value = PAUSED
        player.pause()
    }

    fun resumeSong() {
        liveDataPlayerState.value = PLAYING
        player.start()
    }

    fun nextSong(context: Context) {
        if (isShuffle) {
            shuffleSong()
        } else {
            songPosition++
            if (songPosition >= listMusic.size) songPosition = 0
            playSong(listMusic[songPosition].id, context)

            liveDataSongModel.value = listMusic[songPosition]
        }
    }

    fun backSong(context: Context) {
        if (isShuffle) {
            shuffleSong()
        } else {
            songPosition--
            if (songPosition < 0) songPosition = listMusic.size - 1
            playSong(listMusic[songPosition].id, context)
            liveDataSongModel.value = listMusic[songPosition]
        }
    }

    fun repeatSong() {
        liveDataSongModel.value = listMusic[songPosition]
    }

    fun shuffleSong() {
        var newSong = songPosition
        while (newSong == songPosition)
            newSong = Random.nextInt(listMusic.size - 1)
        songPosition = newSong
        liveDataSongModel.value = listMusic[songPosition]
    }

    fun updateProgress(duration: Int) {
        seekBar.max = duration
        val progressRunner = progressRunner(player)
        seekBar.postDelayed(progressRunner, 1000)
    }

    fun progressRunner(mp: MediaPlayer): Runnable {
        this.player = mp
        val progressRunner: Runnable = object : Runnable {
            override fun run() {
                seekBar.progress = mp.currentPosition
                if (mp.isPlaying) {
                    seekBar.postDelayed(this, 1000)
                }
            }
        }
        return progressRunner
    }

    fun setSeekbar(seekBar: SeekBar) {
        this.seekBar = seekBar
    }

}