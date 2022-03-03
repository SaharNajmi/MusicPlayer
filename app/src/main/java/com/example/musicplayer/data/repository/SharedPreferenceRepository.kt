package com.example.musicplayer.data.repository

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceRepository @Inject constructor(
    private var sharedPreferences: SharedPreferences
) {
    fun setSharedPreferenceInt(prefName: String, defaultValue: Int) {
        sharedPreferences.edit().apply {
            putInt(prefName, defaultValue)
            apply()
        }
    }

    fun getSharedPreferenceInt(prefName: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(prefName, defaultValue)
    }
}