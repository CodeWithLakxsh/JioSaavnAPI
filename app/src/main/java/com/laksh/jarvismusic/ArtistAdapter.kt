package com.laksh.jarvismusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ArtistAdapter(
    private val artists: List<Artist>,
    private val onArtistClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    inner class ArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArtist: ShapeableImageView = view.findViewById(R.id.img_artist_profile)
        val tvName: TextView = view.findViewById(R.id.tv_artist_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist_profile, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        holder.tvName.text = artist.name

        // FIXED: Maps to standard platform resource pointer directly
        Glide.with(holder.itemView.context)
            .load(artist.imageUrl)
            .placeholder(android.R.drawable.sym_def_app_icon)
            .into(holder.imgArtist)

        holder.itemView.setOnClickListener { onArtistClick(artist) }
    }

    override fun getItemCount() = artists.size
}