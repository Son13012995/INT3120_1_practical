/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.artspace

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

@Composable
fun ArtSpaceApp() {
    val context = LocalContext.current // Thêm dòng này
    val artworks = listOf(
        Artwork(
            R.drawable.artwork_1,
            R.string.title_one,
            R.string.artist_one
        ),
        Artwork(
            R.drawable.artwork_2,
            R.string.title_two,
            R.string.artist_two
        ),
        Artwork(
            R.drawable.artwork_3,
            R.string.title_three,
            R.string.artist_three
        )
    )

    var currentArtworkIndex by remember { mutableStateOf(0) }
    val currentArtwork = artworks[currentArtworkIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ArtworkDisplay(
            drawableRes = currentArtwork.drawableRes,
            modifier = Modifier.weight(1f)
        )
        ArtworkTitleAndArtist(
            titleRes = currentArtwork.titleRes,
            artistRes = currentArtwork.artistRes
        )
        Spacer(Modifier.height(32.dp))
        ControlButtons(
            onPreviousClick = {
                currentArtworkIndex = if (currentArtworkIndex > 0) {
                    currentArtworkIndex - 1
                } else {
                    artworks.size - 1
                }
            },
            onNextClick = {
                currentArtworkIndex = if (currentArtworkIndex < artworks.size - 1) {
                    currentArtworkIndex + 1
                } else {
                    0
                }
            },
            onShareClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_subject))
                    putExtra(
                        Intent.EXTRA_TEXT,
                        context.getString(
                            R.string.share_message,
                            context.getString(currentArtwork.titleRes),
                            context.getString(currentArtwork.artistRes)
                        )
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, null))
            }
        )
    }
}

@Composable
fun ArtworkDisplay(
    @DrawableRes drawableRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(16.dp)
            .shadow(10.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = null,
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Composable
fun ArtworkTitleAndArtist(
    @StringRes titleRes: Int,
    @StringRes artistRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = artistRes),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ControlButtons(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(onClick = onPreviousClick) {
            Text(text = stringResource(R.string.previous_button))
        }
        Button(onClick = onNextClick) {
            Text(text = stringResource(R.string.next_button))
        }
        Button(onClick = onShareClick) {
            Text(text = stringResource(R.string.share_button))
        }
    }
}

data class Artwork(
    @DrawableRes val drawableRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val artistRes: Int
)

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}