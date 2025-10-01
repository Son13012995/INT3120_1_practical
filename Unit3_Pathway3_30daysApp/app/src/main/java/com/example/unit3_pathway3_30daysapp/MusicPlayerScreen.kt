//package com.example.unit3_pathway3_30daysapp
//
//import android.media.MediaPlayer
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.PlayArrow
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material.icons.filled.Stop
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.isActive
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MusicPlayerScreen(mediaPlayer: MediaPlayer) {
//    // 1. Quản lý trạng thái
//
//    var isPlaying by remember { mutableStateOf(false) }
//    var currentPosition by remember { mutableStateOf(0) }
//    val totalTime = remember { mediaPlayer.duration.toFloat() }
//
//
//    LaunchedEffect(isPlaying) {
//
//        while (isPlaying && isActive) {
//            if (mediaPlayer.isPlaying) {
//                currentPosition = mediaPlayer.currentPosition
//            }
//            delay(1000)
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Compose Music Player") })
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(horizontal = 16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "${formatTime(currentPosition)} / ${formatTime(totalTime.toInt())}",
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//            Spacer(Modifier.height(16.dp))
//
//
//            Slider(
//                value = currentPosition.toFloat(),
//                onValueChange = { newValue ->
//
//                    currentPosition = newValue.toInt()
//                },
//                onValueChangeFinished = {
//                    mediaPlayer.seekTo(currentPosition)
//                },
//                valueRange = 0f..totalTime,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(Modifier.height(32.dp))
//
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//
//                Button(onClick = {
//                    mediaPlayer.seekTo(0)
//                    currentPosition = 0
//                    if (!isPlaying) {
//                        mediaPlayer.start()
//                        isPlaying = true
//                    }
//                }) {
//                    Icon(Icons.Filled.Refresh, contentDescription = "Tua lại")
//                }
//
//
//                Button(
//                    onClick = {
//                        if (isPlaying) {
//                            mediaPlayer.pause()
//                        } else {
//                            mediaPlayer.start()
//                        }
//                        isPlaying = !isPlaying
//                    },
//                    modifier = Modifier.size(72.dp)
//                ) {
//                    Icon(
//                        if (isPlaying) Icons.Filled.Stop else Icons.Filled.PlayArrow,
//                        contentDescription = if (isPlaying) "Tạm dừng" else "Phát",
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//fun formatTime(ms: Int): String {
//    val seconds = ms / 1000
//    val minutes = seconds / 60
//    val remainingSeconds = seconds % 60
//    return String.format("%02d:%02d", minutes, remainingSeconds)
//}
//
