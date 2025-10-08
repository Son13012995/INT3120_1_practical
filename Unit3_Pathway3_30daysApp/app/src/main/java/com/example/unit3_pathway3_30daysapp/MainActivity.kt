package com.example.unit3_pathway3_30daysapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.unit3_pathway3_30daysapp.ui.theme.Unit3_Pathway3_30daysAppTheme

import android.media.MediaPlayer

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
import com.example.unit3_pathway3_30daysapp.model.MusicRepository


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Unit3_Pathway3_30daysAppTheme {
                val context = LocalContext.current
                val playlist = MusicRepository.playlist

                // Trạng thái chỉ mục bài hát hiện tại
                var currentSongIndex by remember { mutableStateOf(0) }

                // MediaPlayer được quản lý bên trong remember.
                // Khi currentSongIndex thay đổi, MediaPlayer sẽ được khởi tạo lại.
                val mediaPlayer by remember(currentSongIndex) {
                    val songId = playlist[currentSongIndex].musicRes
                    val player = MediaPlayer.create(context, songId)

                    // Thêm listener tự động chuyển bài
                    player.setOnCompletionListener {
                        currentSongIndex = (currentSongIndex + 1) % playlist.size
                    }

                    mutableStateOf(player)
                }

                DisposableEffect(mediaPlayer) {
                    onDispose {
                        mediaPlayer.stop()
                        mediaPlayer.release()
                    }
                }

                FullPlayerScreen(
                    mediaPlayer = mediaPlayer,
                    playlist = playlist,
                    currentSongIndex = currentSongIndex,
                    onNext = {
                        // Chuyển sang bài tiếp theo (Vòng lặp)
                        currentSongIndex = (currentSongIndex + 1) % playlist.size
                    },
                    onPrevious = {
                        // Chuyển về bài trước (Vòng lặp)
                        currentSongIndex = (currentSongIndex - 1 + playlist.size) % playlist.size
                    }
                )
            }
        }
    }
}
