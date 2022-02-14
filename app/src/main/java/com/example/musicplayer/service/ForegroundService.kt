package com.example.musicplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.musicplayer.utils.ActionPlaying
import com.example.musicplayer.utils.Constants


class ForegroundService : Service() {
    val musicBind: IBinder = MusicBinder()
    lateinit var actionPlaying: ActionPlaying

    override fun onBind(p0: Intent?): IBinder? {
        return musicBind
    }

    inner class MusicBinder : Binder() {
        fun getService() = this@ForegroundService
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val actionName = intent.getStringExtra("myActionName")
        if (actionName != null) {
            when (actionName) {
                //play
                Constants.PLAY_ACTION -> actionPlaying.playClicked()
                //next
                Constants.NEXT_ACTION -> actionPlaying.nextClicked()
                //stop
                Constants.STOPFOREGROUND_ACTION -> {
                    actionPlaying.closeClicked()
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
        return START_STICKY
    }

    fun setCallBack(actionPlaying: ActionPlaying) {
        this.actionPlaying = actionPlaying
    }

}