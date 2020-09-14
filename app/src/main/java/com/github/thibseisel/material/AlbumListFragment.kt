package com.github.thibseisel.material

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.transition.Hold
import kotlinx.android.synthetic.main.fragment_album_list.*

class AlbumListFragment : Fragment(R.layout.fragment_album_list) {
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AlbumAdapter(this, ::onAlbumSelected)
        album_list.adapter = adapter

        viewModel.albums.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            requireParentFragment().startPostponedEnterTransitionWhenDrawn()
        }
    }

    private fun onAlbumSelected(album: Album, holder: AlbumHolder) {
        requireParentFragment().apply {
            exitTransition = Hold().apply {
                duration = 300
                addTarget(R.id.fragment_home)
            }
            reenterTransition = null
        }

        val transitionExtras = FragmentNavigatorExtras(holder.itemView to "album_${album.id}")
        val toAlbumDetail = HomeFragmentDirections.actionHomeToAlbumDetail(album.id)
        findNavController().navigate(toAlbumDetail, transitionExtras)
    }

    private class AlbumAdapter(
        fragment: Fragment,
        private val albumSelected: (Album, AlbumHolder) -> Unit
    ) : ListAdapter<Album, AlbumHolder>(AlbumDiffCallback()) {

        private val inflater = LayoutInflater.from(fragment.requireContext())
        private val glide = Glide.with(fragment).asBitmap()
            .dontTransform()
            .disallowHardwareConfig()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
            val itemView = inflater.inflate(R.layout.album_grid_item, parent, false)
            return AlbumHolder(itemView)
        }

        override fun getItemId(position: Int): Long = when {
            hasStableIds() -> getItem(position).id
            else -> -1
        }

        override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
            val album = getItem(position)
            holder.itemView.transitionName = "album_${album.id}"
            holder.title.text = album.title
            holder.subtitle.text = album.artist
            glide.load(album.artworkUrl).into(holder.artwork)

            holder.itemView.setOnClickListener {
                val selectedAlbum = getItem(holder.adapterPosition)
                albumSelected(selectedAlbum, holder)
            }
        }
    }

    class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {

        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }

    private class AlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val subtitle: TextView = itemView.findViewById(R.id.artist)
        val artwork: ImageView = itemView.findViewById(R.id.album_art_view)
    }
}