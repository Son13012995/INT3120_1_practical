package com.example.unit3_pathway3_30daysapp.model


import androidx.annotation.DrawableRes
import androidx.annotation.RawRes // Cần RawRes cho tệp nhạc
import com.example.unit3_pathway3_30daysapp.R

data class Song(
    val title: String,
    val artist: String,
    @DrawableRes val coverRes: Int,
    @RawRes val musicRes: Int
)

object MusicRepository {

    val playlist = listOf(
        Song(
            title = "Gio anh noi dau",
            artist = "SB",
            coverRes = R.drawable.album_cover_1,
            musicRes = R.raw.music1
        ),
        Song(
            title = "dap vo kay dan",
            artist = "S.S",
            coverRes = R.drawable.album_cover_2,
            musicRes = R.raw.music2
        ),
        Song(
            title = "Lope",
            artist = "FUU",
            coverRes = R.drawable.album_cover_3,
            musicRes = R.raw.music3
        ),
        Song(
        title = "Khuon mat dang thuong",
        artist = "MTP",
        coverRes = R.drawable.album_cover_4,
        musicRes = R.raw.music4
    )
    )
}
