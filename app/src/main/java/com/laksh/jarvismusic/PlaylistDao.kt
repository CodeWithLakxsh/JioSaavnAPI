package com.laksh.jarvismusic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {
    @Insert
    fun insertPlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): List<Playlist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongToPlaylist(song: PlaylistSong)

    @Query("SELECT * FROM playlist_songs WHERE playlistId = :id")
    fun getSongsInPlaylist(id: Int): List<PlaylistSong>

    @Query("SELECT COUNT(*) FROM playlists")
    fun getPlaylistCount(): Int
}