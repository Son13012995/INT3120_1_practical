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
import androidx.compose.material3.TopAppBarDefaults // Thêm import mới
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amphibians.ui.theme.AmphibiansTheme
import com.example.amphibians.ui.theme.screens.AmphibiansViewModel
import com.example.amphibians.ui.theme.screens.HomeScreen

class MainActivity : ComponentActivity() {


    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun AmphibiansTopAppBar(modifier: Modifier = Modifier) {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            modifier = modifier
        )
    }

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
                        topBar = { AmphibiansTopAppBar() }
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