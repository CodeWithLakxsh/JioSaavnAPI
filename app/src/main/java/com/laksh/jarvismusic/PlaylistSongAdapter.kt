package com.laksh.jarvismusic

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PlaylistSongAdapter(
    private val songs: List<PlaylistSong>,
    private val onSongClick: (PlaylistSong) -> Unit
) : RecyclerView.Adapter<PlaylistSongAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArt: ImageView = view.findViewById(R.id.img_liked_song)
        val tvTitle: TextView = view.findViewById(R.id.tv_liked_title)
        val tvArtist: TextView = view.findViewById(R.id.tv_liked_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Reusing your awesome liked song layout!
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_liked_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist

        Glide.with(holder.itemView.context).load(song.imageUrl).into(holder.imgArt)

        holder.itemView.setOnClickListener { onSongClick(song) }
    }

    override fun getItemCount() = songs.size
}