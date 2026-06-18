package com.laksh.jarvismusic

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikedSongsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LikedSongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_songs)

        // Handle the Back Button
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.rv_liked_songs)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadLikedSongs()
    }

    private fun loadLikedSongs() {
        val db = AppDatabase.getDatabase(this)

        // Fetch from database in background
        lifecycleScope.launch(Dispatchers.IO) {
            val songsList = db.songDao().getAllLikedSongs()

            // Bring data back to UI thread
            withContext(Dispatchers.Main) {
                adapter = LikedSongsAdapter(songsList) { clickedSong ->

                    // --- THE FIX: Send data back to MainActivity instead of toasting ---
                    val resultIntent = Intent()
                    resultIntent.putExtra("song_id", clickedSong.id)
                    resultIntent.putExtra("title", clickedSong.title)
                    resultIntent.putExtra("artist", clickedSong.artist)
                    resultIntent.putExtra("image_url", clickedSong.imageUrl)
                    resultIntent.putExtra("audio_url", clickedSong.audioUrl)

                    // Tell MainActivity it was successful and pass the intent
                    setResult(RESULT_OK, resultIntent)

                    // Close the Liked Songs page so the player comes into view
                    finish()
                }
                recyclerView.adapter = adapter
            }
        }
    }
}