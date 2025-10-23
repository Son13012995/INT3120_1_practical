//package com.example.racetracker.ui
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.safeDrawingPadding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.Icon
//import androidx.compose.material3.LinearProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.racetracker.R
//import com.example.racetracker.ui.theme.RaceTrackerTheme
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//
//
//@Composable
//fun RaceTrackerApp() {
//    val playerOne = remember {
//        RaceParticipant(name = "Player 1", progressIncrement = 10)
//    }
//    val playerTwo = remember {
//        RaceParticipant(name = "Player 2", progressIncrement = 20)
//    }
//    // Dùng rememberSaveable
//    var raceInProgress by remember(key1 = "raceState") { mutableStateOf(false) }
//    var isRaceFinished by remember { mutableStateOf(false) }
//
//
//    // Đặt LaunchedEffect phụ thuộc vào cả raceInProgress và isRaceFinished
//    LaunchedEffect(raceInProgress, isRaceFinished) {
//        if (raceInProgress && !isRaceFinished) {
//            coroutineScope {
//                val scope = this
//                val onWinCallback: (Boolean) -> Unit = { winner ->
//                    if (winner) {
//                        isRaceFinished = true
//                        raceInProgress = false
//                        scope.coroutineContext.cancel()
//                    }
//                }
//
//
//                launch { playerOne.run(onWinCallback) }
//                launch { playerTwo.run(onWinCallback) }
//            }
//        }
//    }
//
//    RaceTrackerScreen(
//        playerOne = playerOne,
//        playerTwo = playerTwo,
//        isRunning = raceInProgress,
//        isRaceFinished= isRaceFinished,
//        onRunStateChange = { raceInProgress = it },
//        onFinishedStateChange = { isRaceFinished = it },
//        modifier = Modifier
//            .statusBarsPadding()
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .safeDrawingPadding()
//            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
//    )
//}
//
//@Composable
//private fun Winner(
//    player: RaceParticipant
//){
//    Text(
//        text = "${player.name} is the winner",
//        style = MaterialTheme.typography.bodyLarge
//    )
//}
//
//@Composable
//private fun RaceTrackerScreen(
//    playerOne: RaceParticipant,
//    playerTwo: RaceParticipant,
//    isRunning: Boolean,
//    isRaceFinished: Boolean,
//    onRunStateChange: (Boolean) -> Unit,
//    onFinishedStateChange: (Boolean) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = stringResource(R.string.run_a_race),
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(dimensionResource(R.dimen.padding_medium)),
//            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_large)),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Icon(
//                painter = painterResource(R.drawable.ic_walk),
//                contentDescription = null,
//                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
//            )
//            // Lặp lại logic hiển thị StatusIndicator và Spacer để tối ưu hóa UI
//            listOf(playerOne, playerTwo).forEachIndexed { index, player ->
//                StatusIndicator(
//                    participantName = player.name,
//                    currentProgress = player.currentProgress,
//                    maxProgress = stringResource(
//                        R.string.progress_percentage,
//                        player.maxProgress
//                    ),
//                    progressFactor = player.progressFactor,
//                    modifier = Modifier.fillMaxWidth()
//                )
//                if (index == 0) {
//                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_large)))
//                }
//            }
//
//            RaceControls(
//                isRunning = isRunning,
//                isRaceFinished = isRaceFinished,
//                onRunStateChange = onRunStateChange,
//                onReset = {
//                    playerOne.reset()
//                    playerTwo.reset()
//                    onRunStateChange(false)
//                    onFinishedStateChange(false)
//                },
//                modifier = Modifier.fillMaxWidth(),
//            )
//
//            listOf(playerOne, playerTwo).filter { it.isWinner }.forEach { Winner(player = it) }
//        }
//    }
//}
//
//@Composable
//private fun StatusIndicator(
//    participantName: String,
//    currentProgress: Int,
//    maxProgress: String,
//    progressFactor: Float,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//    ) {
//        Text(
//            text = participantName,
//            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small))
//        )
//        Column(
//            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
//        ) {
//            LinearProgressIndicator(
//                progress = progressFactor,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(dimensionResource(R.dimen.progress_indicator_height))
//                    .clip(RoundedCornerShape(dimensionResource(R.dimen.progress_indicator_corner_radius)))
//            )
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = stringResource(R.string.progress_percentage, currentProgress),
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.weight(1f)
//                )
//                Text(
//                    text = maxProgress,
//                    textAlign = TextAlign.End,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun RaceControls(
//    onRunStateChange: (Boolean) -> Unit,
//    onReset: () -> Unit,
//    modifier: Modifier = Modifier,
//    isRunning: Boolean = true,
//    isRaceFinished: Boolean
//) {
//    Column(
//        modifier = modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
//        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
//    ) {
//        Button(
//            onClick = { onRunStateChange(!isRunning) },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !isRaceFinished
//        ) {
//            Text(if (isRunning) stringResource(R.string.pause) else stringResource(R.string.start))
//        }
//        OutlinedButton(
//            onClick = onReset,
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            Text(stringResource(R.string.reset))
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun RaceTrackerAppPreview() {
//    RaceTrackerTheme {
//        RaceTrackerApp()
//    }
//}
package com.example.racetracker.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.racetracker.R
import com.example.racetracker.ui.theme.RaceTrackerTheme
import kotlinx.coroutines.launch

@Composable
fun RaceTrackerApp() {

    val playerOne = remember { RaceParticipant(name = "Người 1", progressIncrement = 30) }
    val playerTwo = remember { RaceParticipant(name = "Người 2", progressIncrement = 10) }


    var dayCount by rememberSaveable { mutableStateOf(0) }
    var raceFinished by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    val advanceDay: () -> Unit = {
        scope.launch {
            dayCount++

            playerOne.advanceDay()

            playerTwo.advanceDay()

            if (playerOne.isWinner || playerTwo.isWinner) {
                raceFinished = true
            }
        }
    }


    val onReset: () -> Unit = {
        playerOne.reset()
        playerTwo.reset()
        dayCount = 0
        raceFinished = false
    }

    RaceTrackerScreen(
        playerOne = playerOne,
        playerTwo = playerTwo,
        dayCount = dayCount,
        raceFinished = raceFinished,
        onAdvanceDay = advanceDay,
        onReset = onReset,
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
    )
}

@Composable
private fun RaceTrackerScreen(
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant,
    dayCount: Int,
    raceFinished: Boolean,
    onAdvanceDay: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RaceTrackerAsDay",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
        )
        Text(
            text = "Ngày: $dayCount",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_large))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            RaceBarChart(
                playerOne = playerOne,
                playerTwo = playerTwo,
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.padding_large))
                    .fillMaxWidth()
            )


            RaceControlsDaySim(
                onAdvanceDay = onAdvanceDay,
                onReset = onReset,
                raceFinished = raceFinished,
                modifier = Modifier.fillMaxWidth()
            )


            if (raceFinished) {
                listOf(playerOne, playerTwo).filter { it.isWinner }.forEach { Winner(player = it) }
            }
        }
    }
}

@Composable
fun RaceBarChart(
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant,
    modifier: Modifier = Modifier
) {

    val maxHeight = dimensionResource(R.dimen.chart_max_height)

    // Layout Row để đặt hai cột cạnh nhau
    Row(
        modifier = modifier.height(maxHeight),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {

        Bar(
            name = playerOne.name,
            progress = playerOne.currentProgress,
            progressFactor = playerOne.progressFactor,
            isWinner = playerOne.isWinner,
            maxHeight = maxHeight,
            color = MaterialTheme.colorScheme.primary
        )


        Bar(
            name = playerTwo.name,
            progress = playerTwo.currentProgress,
            progressFactor = playerTwo.progressFactor,
            isWinner = playerTwo.isWinner,
            maxHeight = maxHeight,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
private fun Bar(
    name: String,
    progress: Int,
    progressFactor: Float,
    isWinner: Boolean,
    maxHeight: Dp,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(dimensionResource(R.dimen.bar_width)) // Cần định nghĩa bar_width
    ) {

        Text(
            text = "$progress%",
            style = MaterialTheme.typography.bodySmall,
            color = if (isWinner) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_extra_small))
        )
        Spacer(
            modifier = Modifier

                .height(maxHeight * progressFactor)
                .fillMaxWidth()
                .background(color)
                .clip(RoundedCornerShape(topStart = dimensionResource(R.dimen.bar_corner_radius), topEnd = dimensionResource(R.dimen.bar_corner_radius)))
        )


        Text(
            text = name.substringBefore(" "),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_extra_small))
        )
    }
}

@Composable
private fun RaceControlsDaySim(
    onAdvanceDay: () -> Unit,
    onReset: () -> Unit,
    raceFinished: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Button(
            onClick = onAdvanceDay,
            modifier = Modifier.fillMaxWidth(),
            enabled = !raceFinished
        ) {
            Text("Tiến Lên Một Ngày")
        }
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.reset))
        }
    }
}

@Composable
private fun Winner(
    player: RaceParticipant
){
    Text(
        text = "${player.name} là người chiến thắng!",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error
    )
}

//package com.example.racetracker.ui
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.horizontalScroll // Giữ lại import này
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.safeDrawingPadding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import com.example.racetracker.R
//import com.example.racetracker.ui.theme.RaceTrackerTheme
//import kotlinx.coroutines.launch
//
//// -------------------------------------------------------------------------------------------------
//// MÔ HÌNH DỮ LIỆU LỊCH SỬ
//// -------------------------------------------------------------------------------------------------
//
//data class DayProgress(
//    val day: Int,
//    val playerOneProgress: Int, // Tiến trình tích lũy của Người 1
//    val playerTwoProgress: Int  // Tiến trình tích lũy của Người 2
//)
//
//// -------------------------------------------------------------------------------------------------
//// HÀM QUẢN LÝ TRẠNG THÁI CHÍNH (MÔ PHỎNG THEO NGÀY VÀ LƯU LỊCH SỬ)
//// -------------------------------------------------------------------------------------------------
//
//@Composable
//fun RaceTrackerApp() {
//    val playerOne = remember { RaceParticipant(name = "Người 1", progressIncrement = 30) }
//    val playerTwo = remember { RaceParticipant(name = "Người 2", progressIncrement = 10) }
//
//    var dayCount by rememberSaveable { mutableStateOf(0) }
//    var raceFinished by rememberSaveable { mutableStateOf(false) }
//
//    var history by rememberSaveable { mutableStateOf(listOf<DayProgress>()) }
//
//    val scope = rememberCoroutineScope()
//
//    val advanceDay: () -> Unit = {
//        scope.launch {
//            if (raceFinished) return@launch
//
//            dayCount++
//
//            playerOne.advanceDay()
//            playerTwo.advanceDay()
//
//            val newProgress = DayProgress(
//                day = dayCount,
//                playerOneProgress = playerOne.currentProgress,
//                playerTwoProgress = playerTwo.currentProgress
//            )
//            history = history + newProgress
//
//            if (playerOne.isWinner || playerTwo.isWinner) {
//                raceFinished = true
//            }
//        }
//    }
//
//    val onReset: () -> Unit = {
//        playerOne.reset()
//        playerTwo.reset()
//        dayCount = 0
//        raceFinished = false
//        history = emptyList()
//    }
//
//    RaceTrackerScreen(
//        history = history,
//        dayCount = dayCount,
//        raceFinished = raceFinished,
//        onAdvanceDay = advanceDay,
//        onReset = onReset,
//        modifier = Modifier
//            .statusBarsPadding()
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .safeDrawingPadding()
//            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
//    )
//}
//
//// -------------------------------------------------------------------------------------------------
//// HÀM UI MÀN HÌNH CHÍNH
//// -------------------------------------------------------------------------------------------------
//
//@Composable
//private fun RaceTrackerScreen(
//    history: List<DayProgress>,
//    dayCount: Int,
//    raceFinished: Boolean,
//    onAdvanceDay: () -> Unit,
//    onReset: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Ractraker",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
//        )
//        Text(
//            text = "Ngày: $dayCount",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_large))
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(dimensionResource(R.dimen.padding_medium)),
//            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_large)),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            // Hiển thị biểu đồ lịch sử hoặc Icon khi chưa có dữ liệu
//            if (history.isNotEmpty()) {
//                RaceBarChart(
//                    history = history,
//                    modifier = Modifier
//                        .padding(vertical = dimensionResource(R.dimen.padding_large))
//
//                )
//            } else {
//                Icon(
//                    painter = painterResource(R.drawable.ic_walk),
//                    contentDescription = null,
//                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)).size(dimensionResource(R.dimen.chart_max_height) / 2),
//                )
//            }
//
//
//            RaceControlsDaySim(
//                onAdvanceDay = onAdvanceDay,
//                onReset = onReset,
//                raceFinished = raceFinished,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            if (raceFinished) {
//                val lastDay = history.last()
//                val playerOneWon = lastDay.playerOneProgress >= 100
//
//                Winner(
//                    player = if (playerOneWon) RaceParticipant("Người 1", 1) else RaceParticipant("Người 2", 1)
//                )
//            }
//        }
//    }
//}
//
//// -------------------------------------------------------------------------------------------------
//// HÀM VẼ BIỂU ĐỒ (ĐÃ SỬA ĐỂ CUỘN NGANG TỐT HƠN)
//// -------------------------------------------------------------------------------------------------
//
//@Composable
//fun RaceBarChart(
//    history: List<DayProgress>,
//    modifier: Modifier = Modifier
//) {
//    val maxHeight = dimensionResource(R.dimen.chart_max_height)
//    val scrollState = rememberScrollState()
//
//    // CHỈNH SỬA QUAN TRỌNG:
//    // Áp dụng height và horizontalScroll cho container bên ngoài
//    Column(modifier = modifier.fillMaxWidth().height(maxHeight + dimensionResource(R.dimen.padding_extra_large)))
//    {
//        Row(
//            // Áp dụng horizontalScroll cho Row bên trong để nó cuộn
//            modifier = Modifier
//                .fillMaxSize()
//                .horizontalScroll(scrollState),
//            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
//            verticalAlignment = Alignment.Bottom
//        ) {
//            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small))) // Đệm đầu
//
//            // LẶP QUA TỪNG NGÀY ĐỂ VẼ TỪNG CẶP CỘT
//            history.forEach { dayProgress ->
//                DayBarGroup(
//                    dayProgress = dayProgress,
//                    maxHeight = maxHeight
//                )
//            }
//
//            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small))) // Đệm cuối
//        }
//    }
//}
//@Composable
//private fun DayBarGroup(
//    dayProgress: DayProgress,
//    maxHeight: Dp
//) {
//    // Nhóm 2 cột trong một Column
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small))
//    ) {
//        // Text hiển thị số ngày
//        Text(text = "Ngày ${dayProgress.day}", style = MaterialTheme.typography.bodySmall)
//
//        // Row chứa 2 cột (cột 1 và cột 2 đặt cạnh nhau)
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small)),
//            verticalAlignment = Alignment.Bottom
//        ) {
//            // Cột cho Người Chơi 1
//            Bar(
//                progress = dayProgress.playerOneProgress,
//                progressFactor = dayProgress.playerOneProgress / 100f,
//                maxHeight = maxHeight,
//                color = MaterialTheme.colorScheme.primary
//            )
//
//            // Cột cho Người Chơi 2
//            Bar(
//                progress = dayProgress.playerTwoProgress,
//                progressFactor = dayProgress.playerTwoProgress / 100f,
//                maxHeight = maxHeight,
//                color = MaterialTheme.colorScheme.tertiary
//            )
//        }
//    }
//}
//
//
//@Composable
//private fun Bar(
//    progress: Int,
//    progressFactor: Float,
//    maxHeight: Dp,
//    color: Color
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.width(dimensionResource(R.dimen.bar_width))
//    ) {
//        // Text hiển thị % tiến độ trên đỉnh cột
//        Text(
//            text = "$progress%",
//            style = MaterialTheme.typography.bodySmall,
//            color = if (progress >= 100) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
//            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_extra_small))
//        )
//
//        // Cột thực tế
//        Spacer(
//            modifier = Modifier
//                // LƯU Ý: Đảm bảo chiều cao không vượt quá maxHeight và có tỷ lệ đúng
//                .height(maxHeight * progressFactor.coerceIn(0f, 1f))
//                .fillMaxWidth()
//                .background(color)
//                .clip(RoundedCornerShape(topStart = dimensionResource(R.dimen.bar_corner_radius), topEnd = dimensionResource(R.dimen.bar_corner_radius)))
//        )
//    }
//}
//
//
//// -------------------------------------------------------------------------------------------------
//// CÁC HÀM UI PHỤ TRỢ
//// -------------------------------------------------------------------------------------------------
//
//@Composable
//private fun RaceControlsDaySim(
//    onAdvanceDay: () -> Unit,
//    onReset: () -> Unit,
//    raceFinished: Boolean,
//    modifier: Modifier = Modifier,
//) {
//    Column(
//        modifier = modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
//        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
//    ) {
//        Button(
//            onClick = onAdvanceDay,
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !raceFinished
//        ) {
//            Text("Tiến Lên Một Ngày")
//        }
//        OutlinedButton(
//            onClick = onReset,
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            Text(stringResource(R.string.reset))
//        }
//    }
//}
//
//@Composable
//private fun Winner(
//    player: RaceParticipant
//){
//    Text(
//        text = "${player.name} là người chiến thắng!",
//        style = MaterialTheme.typography.bodyLarge,
//        color = MaterialTheme.colorScheme.error
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun RaceTrackerAppPreview() {
//    RaceTrackerTheme {
//        RaceTrackerApp()
//    }
//}