package com.example.musicplayer.view.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.data.api.APiService
import com.example.musicplayer.data.db.LyricsDatabase
import com.example.musicplayer.data.model.Lyric
import com.example.musicplayer.data.model.SongModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LyricsViewModel(application: Application) : AndroidViewModel(application) {
    val lyrics = MutableLiveData<Lyric>()
    val findLyrics = MutableLiveData(false)
    var repository: LyricsRepository

    init {
        val musicDao = LyricsDatabase.getInstance(getApplication()).lyricsDao()
        repository = LyricsRepository(musicDao)
    }

    fun searchLyrics(artist: String, title: String) {
        APiService.api.search(artist, title).enqueue(object : Callback<Lyric> {
            override fun onResponse(call: Call<Lyric>, response: Response<Lyric>) {
                if (response.isSuccessful && response.body() != null) {
                    lyrics.postValue(response.body())
                    findLyrics.value = true
                } else {
                    /* val jsonString =
                         """{"lyrics":"","error":"${response.errorBody()?.toString()}"}"""
                     lyrics.postValue(Gson().fromJson(jsonString, Lyric::class.java))*/
                    lyrics.postValue(Lyric(null, "Not Find!!!"))
                    findLyrics.value = false
                }
            }

            override fun onFailure(call: Call<Lyric>, t: Throwable) {
                lyrics.postValue(Lyric(null, "Network error, please try again"))
                findLyrics.value = false
            }
        })
    }

    fun insert(songModel: SongModel) {
        songModel.isLyrics = true
        repository.insert(songModel)
    }

}