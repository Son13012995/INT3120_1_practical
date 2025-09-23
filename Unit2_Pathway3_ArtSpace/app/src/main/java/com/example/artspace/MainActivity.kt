package com.example.artspace

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artspace.ui.theme.ArtSpaceTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") { SplashScreen(navController) }
                    composable("art") { ArtSpaceApp() }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        navController.navigate("art") {
            popUpTo("splash") { inclusive = true } // Xóa splash khỏi back stack
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chào mừng đến với Art Space!",
                fontSize = 50.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ArtSpaceApp() {
    val context = LocalContext.current
    val artworks = listOf(
        Artwork(R.drawable.artwork_1, R.string.title_one, R.string.artist_one),
        Artwork(R.drawable.artwork_2, R.string.title_two, R.string.artist_two),
        Artwork(R.drawable.artwork_3, R.string.title_three, R.string.artist_three)
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
                val shareIntent = Intent(Intent.ACTION_SEND).apply { // Vẫn dùng implicit intents khi tương tác với APP ngoài
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
/*

 ------------------------------------------------------------
   - Explicit Intent
   ------------------------------------------------------------

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
                    ArtSpaceApp() // chạy trực tiếp, không có NavHost
                }
            }
        }
    }
}
*/

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
