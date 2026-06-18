package com.laksh.jarvismusic

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistDetailsActivity : AppCompatActivity() {

    private lateinit var rvSongs: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var adapter: PlaylistSongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_details)

        // Initialize Database and UI
        db = AppDatabase.getDatabase(this)
        rvSongs = findViewById(R.id.rv_playlist_songs)
        rvSongs.layoutManager = LinearLayoutManager(this)

        // Setup Back Button
        findViewById<ImageView>(R.id.btn_back_playlist_details).setOnClickListener {
            finish()
        }

        // Catch the dynamic data passed from PlaylistsActivity
        val playlistId = intent.getIntExtra("PLAYLIST_ID", -1)
        val playlistName = intent.getStringExtra("PLAYLIST_NAME") ?: "Your Playlist"

        // Set the dynamic title in the header
        findViewById<TextView>(R.id.tv_dynamic_playlist_title).text = playlistName

        // Load songs if the ID is valid
        if (playlistId != -1) {
            loadSongsForPlaylist(playlistId)
        } else {
            Toast.makeText(this, "Error: Playlist not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadSongsForPlaylist(playlistId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Fetch songs linked to this playlist ID from the database
            val songsList = db.playlistDao().getSongsInPlaylist(playlistId)

            withContext(Dispatchers.Main) {
                if (songsList.isEmpty()) {
                    Toast.makeText(this@PlaylistDetailsActivity, "This playlist is empty!", Toast.LENGTH_SHORT).show()
                }

                // Setup the adapter with a click listener
                adapter = PlaylistSongAdapter(songsList) { clickedSong ->
                    // --- THE BRIDGE ---
                    // Package the song data to send back to MainActivity via the Result Launcher
                    val resultIntent = Intent()

                    // CRITICAL: Ensure these keys match playDatabaseLauncher in MainActivity
                    resultIntent.putExtra("audio_url", clickedSong.audioUrl)
                    resultIntent.putExtra("title", clickedSong.title)
                    resultIntent.putExtra("artist", clickedSong.artist)
                    resultIntent.putExtra("image_url", clickedSong.imageUrl)
                    resultIntent.putExtra("song_id", clickedSong.songId)

                    // Set result as OK and close this activity
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
                rvSongs.adapter = adapter
            }
        }
    }
}