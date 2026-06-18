package com.laksh.jarvismusic.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JioSaavnApi {
    @GET("result/")
    fun searchSongs(
        @Query("query") searchQuery: String,
        @Query("n") n: Int = 20   // changed from "limit" to "n"
    ): Call<List<ApiSong>>
}