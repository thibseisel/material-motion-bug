package com.github.thibseisel.material

data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val artworkUrl: String?
)

data class Playlist(
    val id: Long,
    val title: String
)

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val artworkUrl: String?
)