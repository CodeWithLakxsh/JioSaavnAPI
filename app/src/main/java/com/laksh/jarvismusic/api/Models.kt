package com.laksh.jarvismusic.api

import com.google.gson.annotations.SerializedName

// The top-level response from your new Python server is a direct List of songs,
// but keeping this wrapper ensures Retrofit doesn't break if your interface expects a parent object.
data class SearchResponse(
    val data: SearchData?
)

data class SearchData(
    val results: List<ApiSong>?
)

data class ApiSong(
    val id: String?,

    // Matches the "song" key from your new server output
    @SerializedName("song")
    val song: String?,

    // Matches the "singers" key from your new server output
    @SerializedName("singers")
    val singers: String?,

    // Your new server provides the image as a direct String URL, not a List anymore
    @SerializedName("image")
    val image: String?,

    // Matches the high-quality streaming file link from your server output
    @SerializedName("media_url")
    val media_url: String?,

    // Matches the album title string
    @SerializedName("album")
    val album: String?
)