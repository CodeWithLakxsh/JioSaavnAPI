package com.laksh.jarvismusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laksh.jarvismusic.api.ApiSong

class HomeRowAdapter(
    private val songs: List<ApiSong>,
    private val onSongClick: (ApiSong) -> Unit
) : RecyclerView.Adapter<HomeRowAdapter.RowViewHolder>() {

    inner class RowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAlbumArt: ImageView = view.findViewById(R.id.img_row_album_art)
        val tvTitle: TextView = view.findViewById(R.id.tv_row_title)
        val tvArtist: TextView = view.findViewById(R.id.tv_row_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horizontal_row, parent, false)
        return RowViewHolder(view)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val song = songs[position]

        holder.tvTitle.text = song.song
        holder.tvArtist.text = song.singers ?: "Unknown Artist"

        val imageUrl = song.image
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.drawable.sym_def_app_icon)
            .into(holder.imgAlbumArt)

        holder.itemView.setOnClickListener { onSongClick(song) }
    }

    override fun getItemCount() = songs.size
}