package com.laksh.jarvismusic

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistsActivity : AppCompatActivity() {

    private lateinit var rvPlaylists: RecyclerView
    private lateinit var db: AppDatabase

    // --- THE BRIDGE LAUNCHER (DO NOT REMOVE) ---
    private val playlistDetailsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            setResult(RESULT_OK, result.data)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlists)

        db = AppDatabase.getDatabase(this)
        rvPlaylists = findViewById(R.id.rv_playlists)
        rvPlaylists.layoutManager = LinearLayoutManager(this)

        findViewById<ImageView>(R.id.btn_back_playlists).setOnClickListener { finish() }

        findViewById<ExtendedFloatingActionButton>(R.id.btn_create_playlist).setOnClickListener {
            showCreatePlaylistDialog()
        }

        loadPlaylists()
    }

    private fun loadPlaylists() {
        lifecycleScope.launch(Dispatchers.IO) {
            val playlists = db.playlistDao().getAllPlaylists()
            withContext(Dispatchers.Main) {
                rvPlaylists.adapter = PlaylistAdapter(playlists) { clickedPlaylist ->
                    val intent = Intent(this@PlaylistsActivity, PlaylistDetailsActivity::class.java)
                    intent.putExtra("PLAYLIST_ID", clickedPlaylist.playlistId)
                    intent.putExtra("PLAYLIST_NAME", clickedPlaylist.name)
                    playlistDetailsLauncher.launch(intent)
                }
            }
        }
    }

    // THE MAGIC POPUP (FIXED FOR VISIBILITY)
    private fun showCreatePlaylistDialog() {
        // Force the material dark theme for the dialog context
        val themedContext = ContextThemeWrapper(this, com.google.android.material.R.style.Theme_MaterialComponents_Dialog_Alert)
        val input = EditText(themedContext)

        input.hint = "Playlist Name"
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.LTGRAY)

        // Force a dark background for the typing box itself to guarantee visibility
        input.setBackgroundColor(Color.parseColor("#1A1A1A"))
        input.setPadding(50, 50, 50, 50)
        input.gravity = Gravity.CENTER_VERTICAL

        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(60, 40, 60, 40)
        input.layoutParams = params
        container.addView(input)

        val dialog = AlertDialog.Builder(themedContext)
            .setTitle("New Playlist")
            .setView(container)
            .setPositiveButton("Create") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.playlistDao().insertPlaylist(Playlist(name = name))
                        loadPlaylists()
                    }
                } else {
                    Toast.makeText(this, "Playlist name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1DB954"))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)

            // Force the window background to be dark to ensure the text pops
            dialog.window?.setBackgroundDrawableResource(android.R.color.background_dark)
        }

        dialog.show()
    }
}