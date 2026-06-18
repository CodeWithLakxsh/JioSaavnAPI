package com.laksh.jarvismusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laksh.jarvismusic.api.ApiSong

class QueueAdapter(
    private val queueList: MutableList<ApiSong>,
    private val onRemoveClick: (Int) -> Unit
) : RecyclerView.Adapter<QueueAdapter.QueueViewHolder>() {

    inner class QueueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArt: ImageView = view.findViewById(R.id.img_queue_art)
        val tvTitle: TextView = view.findViewById(R.id.tv_queue_title)
        val tvArtist: TextView = view.findViewById(R.id.tv_queue_artist)
        val btnRemove: ImageView = view.findViewById(R.id.btn_remove_queue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_queue_song, parent, false)
        return QueueViewHolder(view)
    }

    override fun onBindViewHolder(holder: QueueViewHolder, position: Int) {
        val song = queueList[position]

        // Fixes unresolved references by using updated global properties
        holder.tvTitle.text = song.song
        holder.tvArtist.text = song.singers ?: "Unknown Artist"

        // Grabs the image URL string directly from your server payload
        val imageUrl = song.image ?: ""
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.drawable.sym_def_app_icon)
            .into(holder.imgArt)

        holder.btnRemove.setOnClickListener {
            onRemoveClick(holder.adapterPosition)
        }
    }

    override fun getItemCount() = queueList.size
}