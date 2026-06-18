package com.laksh.jarvismusic

import androidx.room.Entity

@Entity(tableName = "playlist_songs", primaryKeys = ["playlistId", "songId"])
data class PlaylistSong(
    val playlistId: Int,
    val songId: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val audioUrl: String
)