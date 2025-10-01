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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Unit3_Pathway3_30daysAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current


                    val mediaPlayer = remember {
                        // Tạo MediaPlayer với tệp nhạc "music" trong res/raw/
                        MediaPlayer.create(context, R.raw.music)
                    }

                    // 2. Giải phóng MediaPlayer khi Composable bị hủy
                    DisposableEffect(mediaPlayer) {
                        onDispose {
                            // Dọn dẹp tài nguyên khi Activity bị hủy
                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.stop()
                            }
                            mediaPlayer.release()
                        }
                    }

                    MusicPlayerScreen(mediaPlayer = mediaPlayer)
                }
            }
        }
    }
}

