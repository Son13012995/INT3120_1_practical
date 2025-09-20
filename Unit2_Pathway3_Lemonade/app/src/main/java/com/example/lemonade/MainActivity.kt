package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("🍋 Lemonade", fontWeight = FontWeight.Bold) },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color(0xFFFFEB3B), // vàng đậm
                                titleContentColor = Color.Black      // màu chữ
                            )
                        )
                    }
                ) { innerPadding ->

                    LemonTree(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LemonTree(modifier: Modifier = Modifier) {

    var step by rememberSaveable { mutableStateOf(0) }

    // All resource in 1 array
    data class LemonStep(val image: Int, val text: Int, val desc: Int)

    val steps = listOf(
        LemonStep(R.drawable.lemon_tree, R.string.tap_lemon_tree, R.string.lemon_tree),
        LemonStep(R.drawable.lemon_squeeze, R.string.keep_tapping_lemon, R.string.lemon),
        LemonStep(R.drawable.lemon_drink, R.string.tap_lemonade, R.string.glass_of_lemonade),
        LemonStep(R.drawable.lemon_restart, R.string.tap_empty_glass, R.string.empty_glass)
    )

    val current = steps[step]

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AnimatedContent(targetState = current.image, label = "image") { img ->
            Image(
                painter = painterResource(img),
                contentDescription = stringResource(current.desc),
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        step = (step + 1) % steps.size
                    }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedContent(targetState = current.text, label = "text") { txt ->
            Text(
                text = stringResource(txt),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLemonTree() {
    LemonadeTheme {
        LemonTree()
    }
}
