package com.github.thibseisel.material

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import java.util.concurrent.TimeUnit

class PlaylistDetailFragment : Fragment(R.layout.fragment_playlist_detail) {

    private val viewModel by activityViewModels<HomeViewModel>()
    private val navArgs by navArgs<PlaylistDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_container
            duration = 300
            val colorSurface = MaterialColors.getColor(requireContext(), R.attr.colorSurface, 0xFFFFFF)
            setAllContainerColors(colorSurface)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
        view.transitionName = "playlist_${navArgs.playlistId}"

        val adapter = TrackAdapter(this)
        track_list.adapter = adapter

        viewModel.tracks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            startPostponedEnterTransitionWhenDrawn()
        }
    }

    private class TrackAdapter(fragment: Fragment) : ListAdapter<Track, TrackHolder>(TrackDiffCallback()) {

        private val inflater = LayoutInflater.from(fragment.requireContext())
        private val glide = Glide.with(fragment).asBitmap()
            .disallowHardwareConfig()
            .dontTransform()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
            val itemView = inflater.inflate(R.layout.playlist_track_item, parent, false)
            return TrackHolder(itemView)
        }

        override fun onBindViewHolder(holder: TrackHolder, position: Int) {
            val track = getItem(position)
            holder.title.text = track.title
            holder.artist.text = track.artist
            glide.load(track.artworkUrl).into(holder.artwork)
        }
    }

    private class TrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val artist: TextView = itemView.findViewById(R.id.subtitle_view)
        val artwork: ImageView = itemView.findViewById(R.id.album_art_view)
    }

    private class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {

        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }
}