package com.github.thibseisel.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import kotlinx.android.synthetic.main.fragment_playlists.*

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlaylistAdapter(this, ::onPlaylistSelected)
        playlists_recycler.adapter = adapter

        viewModel.playlists.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            requireParentFragment().startPostponedEnterTransitionWhenDrawn()
        }
    }

    private fun onPlaylistSelected(playlist: Playlist, holder: PlaylistHolder) {
        requireParentFragment().apply {
            exitTransition = Hold().apply {
                duration = 300
                addTarget(R.id.fragment_home)
            }
            reenterTransition = null
        }

        val transitionExtras = FragmentNavigatorExtras(
            holder.itemView to "playlist_${playlist.id}"
        )

        val toPlaylistDetail = HomeFragmentDirections.actionHomeToPlaylistDetail(playlist.id)
        findNavController().navigate(toPlaylistDetail, transitionExtras)
    }

    private class PlaylistAdapter(
        fragment: Fragment,
        private val onPlaylistSelected: (Playlist, PlaylistHolder) -> Unit
    ) : ListAdapter<Playlist, PlaylistHolder>(PlaylistDiffCallback()) {

        private val inflater = LayoutInflater.from(fragment.requireContext())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
            val itemView = inflater.inflate(R.layout.playlist_item, parent, false)
            return PlaylistHolder(itemView)
        }

        override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
            val playlist = getItem(position)
            holder.itemView.transitionName = "playlist_${playlist.id}"
            holder.title.text = playlist.title
            holder.icon.setImageResource(R.drawable.ic_audiotrack_24)

            holder.itemView.setOnClickListener {
                val selectedPlaylist = getItem(holder.adapterPosition)
                onPlaylistSelected(selectedPlaylist, holder)
            }
        }
    }

    private class PlaylistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val icon: ImageView = itemView.findViewById(R.id.playlist_icon)
    }

    private class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {

        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }
}