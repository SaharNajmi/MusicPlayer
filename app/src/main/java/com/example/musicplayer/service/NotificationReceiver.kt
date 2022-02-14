package com.example.musicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicplayer.utils.Constants

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intent1 = Intent(context, ForegroundService::class.java)
        if (intent.action != null) {
            when (intent.action) {
                Constants.NEXT_ACTION -> {
                    intent1.putExtra("myActionName", intent.action)
                    context.startService(intent1)
                }
                Constants.PLAY_ACTION -> {
                    intent1.putExtra("myActionName", intent.action)
                    context.startService(intent1)
                }
                Constants.STOPFOREGROUND_ACTION -> {
                    intent1.putExtra("myActionName", intent.action)
                    context.startService(intent1)
                }
            }
        }
    }
}