package com.github.thibseisel.material

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.home_fragment.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition(1000, TimeUnit.MILLISECONDS)

        val adapter = TabAdapter(this)
        pager.adapter = adapter
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Albums"
                1 -> "Playlists"
                else -> error("Invalid position: $position")
            }
        }.attach()
    }

    private class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> AlbumListFragment()
            1 -> PlaylistsFragment()
            else -> error("Invalid position: $position")
        }
    }
}