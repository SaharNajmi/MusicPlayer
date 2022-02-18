package com.example.musicplayer.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.api.APiService
import com.example.musicplayer.data.db.dao.entities.Song
import com.example.musicplayer.data.model.Lyric
import com.example.musicplayer.data.repository.MusicRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LyricsViewModel(val musicRepository: MusicRepository) : ViewModel() {
    val lyrics = MutableLiveData<Lyric>()
    val findLyrics = MutableLiveData(false)

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

    fun update(song: Song) {
        song.isLyrics = true
        musicRepository.update(song)
    }

}