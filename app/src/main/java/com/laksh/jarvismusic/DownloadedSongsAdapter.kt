package com.laksh.jarvismusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class DownloadedSongsAdapter(
    private val files: MutableList<File>,
    private val onSongClick: (File) -> Unit,
    private val onDeleteClick: (File, Int) -> Unit
) : RecyclerView.Adapter<DownloadedSongsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArt: ImageView = view.findViewById(R.id.img_liked_song)
        val tvTitle: TextView = view.findViewById(R.id.tv_liked_title)
        val tvArtist: TextView = view.findViewById(R.id.tv_liked_artist)

        // Grab the delete button from XML
        val btnDelete: ImageView = view.findViewById(R.id.btn_delete_song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liked_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]

        holder.tvTitle.text = file.nameWithoutExtension
        holder.tvArtist.text = "Local File • Jarvis Music"

        holder.imgArt.setImageResource(R.drawable.ic_download)
        holder.imgArt.setColorFilter(android.graphics.Color.parseColor("#1DB954"))

        // Make the delete button visible ONLY on the downloads page
        holder.btnDelete.visibility = View.VISIBLE

        // Standard Click anywhere on the row -> Play Song
        holder.itemView.setOnClickListener {
            onSongClick(file)
        }

        // Click strictly on the Trash Can -> Delete Song
        holder.btnDelete.setOnClickListener {
            onDeleteClick(file, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = files.size
}