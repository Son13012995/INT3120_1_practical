package com.example.racetracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.racetracker.R
import com.example.racetracker.ui.theme.RaceTrackerTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun RaceTrackerApp() {
    val playerOne = remember {
        RaceParticipant(name = "Player 1", progressIncrement = 10)
    }
    val playerTwo = remember {
        RaceParticipant(name = "Player 2", progressIncrement = 20)
    }
    // Dùng rememberSaveable
    var raceInProgress by remember(key1 = "raceState") { mutableStateOf(false) }
    var isRaceFinished by remember { mutableStateOf(false) }


    // Đặt LaunchedEffect phụ thuộc vào cả raceInProgress và isRaceFinished
    LaunchedEffect(raceInProgress, isRaceFinished) {
        if (raceInProgress && !isRaceFinished) {
            coroutineScope {
                val scope = this
                val onWinCallback: (Boolean) -> Unit = { winner ->
                    if (winner) {
                        isRaceFinished = true
                        raceInProgress = false
                        scope.coroutineContext.cancel()
                    }
                }


                launch { playerOne.run(onWinCallback) }
                launch { playerTwo.run(onWinCallback) }
            }
        }
    }

    RaceTrackerScreen(
        playerOne = playerOne,
        playerTwo = playerTwo,
        isRunning = raceInProgress,
        isRaceFinished= isRaceFinished,
        onRunStateChange = { raceInProgress = it },
        onFinishedStateChange = { isRaceFinished = it },
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
    )
}

@Composable
private fun Winner(
    player: RaceParticipant
){
    Text(
        text = "${player.name} is the winner",
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun RaceTrackerScreen(
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant,
    isRunning: Boolean,
    isRaceFinished: Boolean,
    onRunStateChange: (Boolean) -> Unit,
    onFinishedStateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.run_a_race),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_walk),
                contentDescription = null,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            )
            // Lặp lại logic hiển thị StatusIndicator và Spacer để tối ưu hóa UI
            listOf(playerOne, playerTwo).forEachIndexed { index, player ->
                StatusIndicator(
                    participantName = player.name,
                    currentProgress = player.currentProgress,
                    maxProgress = stringResource(
                        R.string.progress_percentage,
                        player.maxProgress
                    ),
                    progressFactor = player.progressFactor,
                    modifier = Modifier.fillMaxWidth()
                )
                if (index == 0) {
                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_large)))
                }
            }

            RaceControls(
                isRunning = isRunning,
                isRaceFinished = isRaceFinished,
                onRunStateChange = onRunStateChange,
                onReset = {
                    playerOne.reset()
                    playerTwo.reset()
                    onRunStateChange(false)
                    onFinishedStateChange(false)
                },
                modifier = Modifier.fillMaxWidth(),
            )

            listOf(playerOne, playerTwo).filter { it.isWinner }.forEach { Winner(player = it) }
        }
    }
}

@Composable
private fun StatusIndicator(
    participantName: String,
    currentProgress: Int,
    maxProgress: String,
    progressFactor: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = participantName,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small))
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            LinearProgressIndicator(
                progress = progressFactor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.progress_indicator_height))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.progress_indicator_corner_radius)))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.progress_percentage, currentProgress),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = maxProgress,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RaceControls(
    onRunStateChange: (Boolean) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    isRunning: Boolean = true,
    isRaceFinished: Boolean
) {
    Column(
        modifier = modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Button(
            onClick = { onRunStateChange(!isRunning) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isRaceFinished
        ) {
            Text(if (isRunning) stringResource(R.string.pause) else stringResource(R.string.start))
        }
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.reset))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RaceTrackerAppPreview() {
    RaceTrackerTheme {
        RaceTrackerApp()
    }
}
