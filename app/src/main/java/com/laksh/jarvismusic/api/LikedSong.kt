package com.laksh.jarvismusic

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_songs")
data class LikedSong(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val audioUrl: String
)