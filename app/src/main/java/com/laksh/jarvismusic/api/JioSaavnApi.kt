package com.laksh.jarvismusic.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

@GET("result/")
suspend fun searchSongs(
    @Query("query") query: String,
    @Query("n") n: Int = 20
): List<ApiSong> // <--- This MUST be List<ApiSong> now