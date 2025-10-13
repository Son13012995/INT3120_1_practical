// MainActivity.kt

package com.example.amphibians
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amphibians.ui.theme.AmphibiansTheme
import com.example.amphibians.ui.theme.screens.AmphibiansViewModel
import com.example.amphibians.ui.theme.screens.HomeScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmphibiansTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val amphibiansViewModel: AmphibiansViewModel =
                        viewModel(factory = AmphibiansViewModel.Factory)
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(stringResource(R.string.app_name)) }
                            )
                        }
                    ) { innerPadding ->
                        HomeScreen(
                            amphibianUiState = amphibiansViewModel.amphibianUiState,
                            retryAction = amphibiansViewModel::getAmphibians,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}