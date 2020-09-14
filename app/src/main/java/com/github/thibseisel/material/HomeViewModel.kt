package com.github.thibseisel.material

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.delay

private const val RANDOM_ACCESS_MEMORIES = "https://www.vinylgourmet.com/120-thickbox_default/daft-punk-random-access-memories-2lp-180gr-download-eu-vinyl-gourmet-best-new-music-2013.jpg"
private const val DISCOVERY = "https://img.discogs.com/IfNxVjRatDT0GOfgNAfdtNy9nDQ=/fit-in/600x600/filters:strip_icc():format(jpeg):mode_rgb():quality(90)/discogs-images/R-136430-1481404839-1193.png.jpg"

class HomeViewModel : ViewModel() {

    private val _albums = listOf(
        Album(
            id = 16,
            title = "Discovery",
            artist = "Daft Punk",
            artworkUrl = DISCOVERY
        ),
        Album(
            id = 42,
            title = "Random Access Memories",
            artist = "Daft Punk",
            artworkUrl = RANDOM_ACCESS_MEMORIES
        )
    )

    private val _playlists = listOf(
        Playlist(
            id = 1837,
            title = "Recently Played"
        ),
        Playlist(
            id = 1736,
            title = "Favorites"
        )
    )

    private val _tracks = listOf(
        Track(
            id = 9837,
            title = "Get Lucky (feat Pharell Williams)",
            artist = "Daft Punk",
            artworkUrl = RANDOM_ACCESS_MEMORIES
        ),
        Track(
            id = 9283,
            title = "Harder Better Faster Stronger",
            artist = "Daft Punk",
            artworkUrl = DISCOVERY
        )
    )

    val albums = liveData {
        delay(200)
        emit(_albums)
    }

    val playlists = liveData {
        delay(500)
        emit(_playlists)
    }

    val tracks = liveData {
        delay(200)
        emit(_tracks)
    }

    fun getAlbum(id: Long): LiveData<Album> = albums.map { albums ->
        albums.find { it.id == id }
            ?: error("Unexpected album id: $id")
    }
}