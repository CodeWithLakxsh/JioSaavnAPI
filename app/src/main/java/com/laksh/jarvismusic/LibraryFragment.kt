package com.laksh.jarvismusic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryFragment : Fragment(R.layout.fragment_library) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Liked Songs Logic
        val likedSongsCard = view.findViewById<View>(R.id.card_liked_songs)
        likedSongsCard?.setOnClickListener {
            (activity as? MainActivity)?.openLikedSongs()
        }

        // 2. Downloaded Songs Logic
        val downloadedCard = view.findViewById<View>(R.id.card_downloaded_songs)
        downloadedCard?.setOnClickListener {
            (activity as? MainActivity)?.openDownloads()
        }

        // 3. Playlists Logic (FIXED FOR PLAYBACK)
        val playlistsCard = view.findViewById<View>(R.id.card_playlists)
        playlistsCard?.setOnClickListener {
            // We MUST call the function in MainActivity.
            // This ensures the player is 'listening' for the song data.
            (activity as? MainActivity)?.openPlaylists()
        }

        // ==============================================
        // DEVELOPER PROMO LINKS
        // ==============================================

        // 4. Visit Website Logic
        val websiteCard = view.findViewById<View>(R.id.btn_visit_website)
        websiteCard?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://codewithlaksh.in/"))
            startActivity(intent)
        }

        // 5. Follow GitHub Logic
        val githubCard = view.findViewById<View>(R.id.btn_follow_github)
        githubCard?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/CodeWithLakxsh"))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateLikedSongsCount()
        updateDownloadedSongsCount()
        updatePlaylistCount()
    }

    private fun updateLikedSongsCount() {
        val tvCount = view?.findViewById<TextView>(R.id.tv_liked_count) ?: return
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            val count = db.songDao().getAllLikedSongs().size
            withContext(Dispatchers.Main) {
                tvCount.text = "$count songs"
            }
        }
    }

    private fun updateDownloadedSongsCount() {
        val tvDownloadedCount = view?.findViewById<TextView>(R.id.tv_downloaded_count) ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val musicDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            val count = musicDir?.listFiles { file -> file.extension == "mp3" }?.size ?: 0

            withContext(Dispatchers.Main) {
                if (count == 1) {
                    tvDownloadedCount.text = "1 song ready for offline"
                } else {
                    tvDownloadedCount.text = "$count songs ready for offline"
                }
            }
        }
    }

    // --- Count Playlists from the Database ---
    private fun updatePlaylistCount() {
        val tvPlaylistCount = view?.findViewById<TextView>(R.id.tv_playlist_count) ?: return
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            val count = db.playlistDao().getPlaylistCount()
            withContext(Dispatchers.Main) {
                if (count == 1) {
                    tvPlaylistCount.text = "1 playlist"
                } else {
                    tvPlaylistCount.text = "$count playlists"
                }
            }
        }
    }
}