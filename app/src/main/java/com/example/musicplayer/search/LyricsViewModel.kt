package com.example.musicplayer.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.model.Lyric
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LyricsViewModel : ViewModel() {
    val lyrics = MutableLiveData<Lyric>()

    fun searchLyrics(artist: String, title: String) {
        APiService.api.search(artist, title).enqueue(object : Callback<Lyric> {
            override fun onResponse(call: Call<Lyric>, response: Response<Lyric>) {
                if (response.isSuccessful && response.body() != null)
                    lyrics.postValue(response.body())
                else {
                    Log.d("TAG", "Not Found!!!")
                    val jsonString =
                        """{"lyrics":"","error":"${response.errorBody()?.toString()}"}"""
                    lyrics.postValue(Gson().fromJson(jsonString, Lyric::class.java))
                }
            }

            override fun onFailure(call: Call<Lyric>, t: Throwable) {
                lyrics.postValue(Lyric("", "Network Error!"))
            }
        })
    }
}