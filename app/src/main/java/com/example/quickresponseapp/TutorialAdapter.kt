package com.example.quickresponseapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView

class TutorialAdapter(private val videos: List<TutorialVideo>) :
    RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder>() {

    inner class TutorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.video_title)
        val playerView: PlayerView = view.findViewById(R.id.player_view)
        var player: ExoPlayer? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tutorial_video, parent, false)
        return TutorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: TutorialViewHolder, position: Int) {
        val context = holder.itemView.context
        val video = videos[position]

        holder.title.text = video.title

        // Release old player if exists
        holder.player?.release()

        val player = ExoPlayer.Builder(context).build()
        holder.player = player

        val uri = Uri.parse("android.resource://${context.packageName}/${video.videoResId}")
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()

        holder.playerView.player = player
    }

    override fun onViewRecycled(holder: TutorialViewHolder) {
        super.onViewRecycled(holder)
        holder.player?.release()
    }

    override fun getItemCount(): Int = videos.size
}