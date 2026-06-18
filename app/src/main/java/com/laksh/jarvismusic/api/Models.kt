package com.laksh.jarvismusic.api

import com.google.gson.annotations.SerializedName

// Since the API now returns a direct List [{}, {}, {}],
// we only need the class representing a single song.
data class ApiSong(
    @SerializedName("id")
    val id: String?,

    @SerializedName("song")
    val song: String?,

    @SerializedName("singers")
    val singers: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("media_url")
    val media_url: String?,

    @SerializedName("album")
    val album: String?
)