package com.example.artspace

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artspace.ui.theme.ArtSpaceTheme
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
            /*onShareClick = { //share text
                val shareIntent = Intent(Intent.ACTION_SEND).apply { // Vẫn dùng implicit intents khi tương tác với APP ngoài
                    type = "text/plain" // loai du lieu: van ban thuan
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
            }*/
                    onShareClick = { //share
                        // 1. Lấy URI của hình ảnh từ tài nguyên drawable
                        val imageUri: Uri? = getUriFromDrawable(context, currentArtwork.drawableRes)

                        if (imageUri != null) {
                            // 2. Tạo một Implicit Intent với hành động ACTION_SEND
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                // 3. Đặt loại dữ liệu là "image/*" để tương thích với các ứng dụng chia sẻ ảnh
                                type = "image/*"
                                // 4. Thêm URI của hình ảnh vào Extras bằng hằng số Intent.EXTRA_STREAM
                                putExtra(Intent.EXTRA_STREAM, imageUri)
                                // 5. Cấp quyền đọc tạm thời cho ứng dụng nhận dữ liệu
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }

                            // 6. Khởi chạy hộp thoại lựa chọn để người dùng chọn ứng dụng
                            context.startActivity(Intent.createChooser(shareIntent, null))
                        }
                    }
        )

    // THÊM NÚT MỞ URL TẠI ĐÂY
    Spacer(Modifier.height(20.dp))
    Button(
        onClick = {
            // Gọi hàm mở URL khi nút được nhấn
            openUrlInBrowser(context, "https://www.facebook.com/tranhoangson1401")
        },
        // Thiết lập kích thước nút nếu cần
        modifier = Modifier.fillMaxWidth(0.6f).align(Alignment.CenterHorizontally)

    ) {
        Text("Visit My Facebook")
    }
        }

}


private fun getUriFromDrawable(context: Context, @DrawableRes drawableRes: Int): Uri? {
    // Lấy bitmap từ drawable
    val drawable = context.resources.getDrawable(drawableRes, null)
    val bitmap = (drawable as BitmapDrawable).bitmap

    // Lưu bitmap vào một tệp tạm thời
    val filesDir = context.cacheDir
    val imageFile = File(filesDir, "share_image.jpeg")

    try {
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

    // Sử dụng FileProvider để tạo URI an toàn
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider", // Tên authority đã khai báo
        imageFile
    )
}

fun openUrlInBrowser(context: Context, url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK  }
    try {
        // Thử khởi động trình duyệt
        context.startActivity(intent)
    } catch (e: Exception) {
        // Bắt và xử lý lỗi nếu không tìm thấy ứng dụng (trình duyệt)
        Toast.makeText(
            context,
            "Không thể mở liên kết. Vui lòng kiểm tra ứng dụng trình duyệt.",
            Toast.LENGTH_LONG
        ).show()
        e.printStackTrace()
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
