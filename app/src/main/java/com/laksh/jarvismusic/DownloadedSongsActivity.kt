package com.laksh.jarvismusic

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class DownloadedSongsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloaded_songs)

        findViewById<ImageView>(R.id.btn_back_downloads).setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.rv_downloaded_songs)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadDownloadedSongs()
    }

    private fun loadDownloadedSongs() {
        val musicDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val songs = musicDir?.listFiles { file -> file.extension == "mp3" }?.toMutableList() ?: mutableListOf()

        if (songs.isEmpty()) {
            Toast.makeText(this, "No downloaded songs found.", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = DownloadedSongsAdapter(
                files = songs,
                onSongClick = { clickedFile ->
                    val resultIntent = Intent()
                    resultIntent.putExtra("file_path", clickedFile.absolutePath)
                    resultIntent.putExtra("file_name", clickedFile.nameWithoutExtension)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                },
                onDeleteClick = { fileToDelete, position ->
                    showDeleteConfirmationDialog(fileToDelete, position, songs)
                }
            )
            recyclerView.adapter = adapter
        }
    }

    // --- THE FIX: Forced Button Colors for the Popup ---
    private fun showDeleteConfirmationDialog(file: File, position: Int, songsList: MutableList<File>) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete Song")
            .setMessage("Are you sure you want to delete '${file.nameWithoutExtension}' from your phone? This cannot be undone.")
            .setPositiveButton("Delete") { d, _ ->
                if (file.exists() && file.delete()) {
                    songsList.removeAt(position)
                    recyclerView.adapter?.notifyItemRemoved(position)
                    recyclerView.adapter?.notifyItemRangeChanged(position, songsList.size)
                    Toast.makeText(this, "Song deleted", Toast.LENGTH_SHORT).show()

                    if (songsList.isEmpty()) {
                        Toast.makeText(this, "No downloaded songs remaining.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error deleting file", Toast.LENGTH_SHORT).show()
                }
                d.dismiss()
            }
            .setNegativeButton("Cancel") { d, _ ->
                d.dismiss()
            }
            .create()

        // This listener waits for the dialog to appear, then paints the buttons!
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF5555")) // Red Delete
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B3B3B3")) // Gray Cancel
        }

        dialog.show()
    }
}