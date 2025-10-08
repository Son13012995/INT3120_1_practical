package com.example.unit3_pathway3_30daysapp

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unit3_pathway3_30daysapp.model.MusicRepository
import com.example.unit3_pathway3_30daysapp.model.Song
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPlayerScreen(
    mediaPlayer: MediaPlayer,
    playlist: List<Song>,
    currentSongIndex: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    // Lấy thông tin bài hát hiện tại
    val currentSong = playlist[currentSongIndex]

    // Quản lý trạng thái
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }
    val totalTime = remember(currentSongIndex) { mediaPlayer.duration.toFloat() }

    // Đồng bộ hóa vị trí nhạc
    LaunchedEffect(currentSongIndex, isPlaying) {
        while (isPlaying && isActive) {
            if (mediaPlayer.isPlaying) {
                currentPosition = mediaPlayer.currentPosition
            }
            delay(1000)
        }
    }

    // Đảm bảo nhạc bắt đầu phát lại khi chuyển bài
    LaunchedEffect(currentSongIndex) {
        if (isPlaying) {
            mediaPlayer.start()
        }
    }


    Scaffold(
        topBar = { TopAppBar(title = { Text("Đang phát") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            // 1. Hình ảnh Album
            Image(
                painter = painterResource(currentSong.coverRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.CenterHorizontally)
            )

            // 2. Thông tin Bài hát
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentSong.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = currentSong.artist,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            // 3. Thanh trượt (Slider)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Slider(
                    value = currentPosition.toFloat(),
                    onValueChange = { currentPosition = it.toInt() },
                    onValueChangeFinished = { mediaPlayer.seekTo(currentPosition) },
                    valueRange = 0f..totalTime,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(formatTime(currentPosition))
                    Text(formatTime(totalTime.toInt()))
                }
            }

            // 4. Nút Điều khiển Mở rộng
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Nút Bài trước
                IconButton(onClick = onPrevious) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = "Bài trước", modifier = Modifier.size(36.dp))
                }

                // Nút PHÁT/TẠM DỪNG LỚN
                FloatingActionButton(
                    onClick = {
                        if (isPlaying) mediaPlayer.pause() else mediaPlayer.start()
                        isPlaying = !isPlaying
                    },
                    modifier = Modifier.size(72.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Tạm dừng" else "Phát",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }

                // Nút Bài tiếp theo
                IconButton(onClick = onNext) {
                    Icon(Icons.Filled.SkipNext, contentDescription = "Bài tiếp theo", modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}

// Hàm formatTime không đổi (chuyển ms sang MM:ss)
fun formatTime(ms: Int): String {
    val seconds = ms / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

// Để hàm Preview hoạt động, bạn cần tạo Mock MediaPlayer
// ... (Sử dụng Mock MediaPlayer và Preview tương tự như hướng dẫn trước)