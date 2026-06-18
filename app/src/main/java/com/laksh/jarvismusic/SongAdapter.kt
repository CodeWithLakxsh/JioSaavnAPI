package com.laksh.jarvismusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.laksh.jarvismusic.api.ApiSong

class SongAdapter(
    private val songList: List<ApiSong>,
    private val onSongClick: (ApiSong) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAlbumArt: ImageView = itemView.findViewById(R.id.iv_album_art)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_song_title)
        val tvArtist: TextView = itemView.findViewById(R.id.tv_song_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songList[position]

        // Clean character properties matching unified server strings
        val cleanTitle = song.song?.replace("&quot;", "\"")
            ?.replace("&amp;", "&")
            ?.replace("&#039;", "'") ?: "Unknown Track"

        val artistName = song.singers ?: "Unknown Artist"

        holder.tvTitle.text = cleanTitle
        holder.tvArtist.text = artistName

        Glide.with(holder.itemView.context)
            .load(song.image ?: "")
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .transform(CenterCrop(), RoundedCorners(16))
            .into(holder.ivAlbumArt)

        // THE STRATEGIC AUTOMATIC QUEUE BRIDGE:
        holder.itemView.setOnClickListener {
            if (!song.media_url.isNullOrEmpty()) {
                // Triggers structural payload execution out to the Fragment constructor parameter lambda
                onSongClick(song)
            } else {
                Toast.makeText(holder.itemView.context, "Audio file unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = songList.size
}