package com.github.thibseisel.material

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_album_detail.*
import java.util.concurrent.TimeUnit

class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {

    private val viewModel by activityViewModels<HomeViewModel>()
    private val navArgs by navArgs<AlbumDetailFragmentArgs>()

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
        view.transitionName = "album_${navArgs.albumId}"

        viewModel.getAlbum(navArgs.albumId).observe(viewLifecycleOwner) {
            startPostponedEnterTransitionWhenDrawn()

            title_view.text = it.title
            subtitle_view.text = it.artist
            Glide.with(this).asBitmap()
                .dontTransform()
                .disallowHardwareConfig()
                .load(it.artworkUrl)
                .into(album_art_view)
        }
    }
}