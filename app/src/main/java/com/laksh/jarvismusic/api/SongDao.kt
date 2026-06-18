package com.laksh.jarvismusic

import androidx.room.*

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: LikedSong)

    @Delete
    suspend fun deleteSong(song: LikedSong)

    // THIS IS THE LINE I FORGOT! It grabs the list of all songs.
    @Query("SELECT * FROM liked_songs")
    suspend fun getAllLikedSongs(): List<LikedSong>

    @Query("SELECT EXISTS(SELECT * FROM liked_songs WHERE id = :songId)")
    suspend fun isLiked(songId: String): Boolean
}