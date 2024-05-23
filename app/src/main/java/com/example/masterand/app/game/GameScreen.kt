package com.example.masterand.app.game

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.masterand.app.game.helpers.RowData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToResultsScreen: (score: Int) -> Unit,
    numberOfColors: Int,
    playerId: Long
) {
    viewModel.numberOfColors = numberOfColors
    viewModel.playerId = playerId

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ScoreText(viewModel.score)

        LazyColumn(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(items = viewModel.data) { row ->

                val rowVisible = remember(row.id) { mutableStateOf(false) }
                if (row.id == viewModel.data.last().id) {
                    LaunchedEffect(key1 = row.id) {
                        rowVisible.value = true
                    }
                }

                AnimatedVisibility(
                    visible = rowVisible.value,
                    enter = expandVertically(
                        expandFrom = Alignment.Top
                    )
                ) {
                    GameRow(
                        selectedColors = row.selectedColors,
                        callbackColors = row.callbackColors,
                        isClickable = row.isClickable,
                        onSelectColorClick = { index ->
                            row.selectedColors[index] = selectNextAvailableColor(
                                availableColors = viewModel.availableColors,
                                selectedColors = row.selectedColors,
                                circularButtonIndex = index
                            )
                        },
                        onCheckClick = {
                            row.callbackColors.clear()
                            row.callbackColors.addAll(
                                checkColors(
                                    pickedColors = row.selectedColors,
                                    correctColors = viewModel.correctColors
                                )
                            )
                            row.isClickable = false
                            if (viewModel.correctColors != row.selectedColors) {
                                viewModel.data.add(RowData(row.id + 1))
                                viewModel.score.intValue++
                            } else {
                                viewModel.isWon.value = true
                            }
                        },
                        isFilled = row.isFilled
                    )
                }
            }
        }

        if (viewModel.isWon.value) {
            HighScoreButtonButton(
                onNavigateToResultsScreen = onNavigateToResultsScreen,
                viewModel
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        LogoutButton(onNavigateToProfileScreen = onNavigateToProfileScreen)

    }
}

@Composable
fun ScoreText(score: MutableIntState) {
    Text(
        text = "Your score: ${score.intValue}",
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier
            .padding(bottom = 48.dp)
    )
}

@Composable
fun GameRow(
    selectedColors: List<Color>,
    callbackColors: List<Color>,
    isClickable: Boolean,
    onSelectColorClick: (circularButtonIndex: Int) -> Unit,
    onCheckClick: () -> Unit,
    isFilled: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SelectableColorsRow(selectedColors, onSelectColorClick)
        Spacer(modifier = Modifier.padding(2.dp))

        AnimatedVisibility(
            visible = !selectedColors.contains(Color.White) && isClickable,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            IconButton(modifier = Modifier
                .size(50.dp)
                .background(color = MaterialTheme.colorScheme.background),
                enabled = isClickable,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ), onClick = { onCheckClick() }) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "icon")
            }
        }

        FeedbackCircles(callbackColors)

    }

}

@Composable
fun SelectableColorsRow(colors: List<Color>, onClick: (circularButtonIndex: Int) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        items(4) { circularButtonIndex ->
            CircularButton(
                onClick = { onClick(circularButtonIndex) },
                color = colors[circularButtonIndex]
            )
        }
    }

}

@Composable
fun CircularButton(onClick: () -> Unit, color: Color) {
    val colorAnimation0 = remember { Animatable(Color.Blue) }
    val colorAnimation1 = remember { Animatable(Color.Red) }

    // Stan wewnętrzny śledzący, czy przycisk jest w stanie początkowym.
    var isInitial by remember { mutableStateOf(true) }

    // Stan wewnętrzny śledzący, czy ma wystąpić animacja.
    var animate by remember { mutableStateOf(false) }

    // Stan wewnętrzny śledzący poprzedni kolor przycisku.
    var oldColor by remember { mutableStateOf(Color.Gray) }

    // Utworzenie obiektu Transition, który kontroluje animacje przy zmianie stanu.
    val transition = updateTransition(isInitial, label = "")

    // Animacja koloru przycisku przy pomocy Transition.
    val animatedColor by transition.animateColor(label = "") { initialState ->
        if (initialState) color else oldColor
    }

//    LaunchedEffect(colors) {
//        colorAnimation0.animateTo(Color.Yellow, animationSpec = tween(500))
//        colorAnimation1.animateTo(Color.Blue, animationSpec = tween(500))
//    }


    Button(
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),

        onClick = {
            onClick()
            animate = true
            oldColor = animatedColor
        }) {

    }

    if (animate) {
        LaunchedEffect(key1 = Unit) {
            repeat(6) {
                delay(100)
                isInitial = !isInitial
            }
            animate = false
        }
    }
}

@Composable
fun FeedbackCircles(colors: List<Color>) {
    Column {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(2.dp)
        ) {
            SmallCircle(color = colors[0], delay = 250)
            SmallCircle(color = colors[1], delay = 500)
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(2.dp)
        ) {
            SmallCircle(color = colors[2], delay = 750)
            SmallCircle(color = colors[3], delay = 1000)
        }
    }
}

@Composable
fun SmallCircle(color: Color, delay: Int) {
    var circleColor by remember { mutableStateOf(color) }

    LaunchedEffect(color) {
        circleColor = color
    }

    val colorTransition = updateTransition(circleColor, label = "")
    val containerColor by colorTransition.animateColor(
        transitionSpec = {
            tween(durationMillis = 250, delayMillis = delay)
        }, label = ""
    ) { targetColor ->
        targetColor
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(containerColor)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .size(20.dp)
    )
    Spacer(modifier = Modifier.padding(2.dp))
}

@Composable
fun HighScoreButtonButton(
    onNavigateToResultsScreen: (score: Int) -> Unit,
    viewModel: GameViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Button(modifier = Modifier
        .padding(16.dp)
        .wrapContentHeight(Alignment.Top),
        onClick = {
            coroutineScope.launch {
                viewModel.saveNewScore()
            }
            onNavigateToResultsScreen(viewModel.score.intValue)
        }) {
        Text("High score")
    }
}

@Composable
fun LogoutButton(onNavigateToProfileScreen: () -> Unit) {
    Button(modifier = Modifier.padding(16.dp), onClick = { onNavigateToProfileScreen() }) {
        Text(text = "Logout")
    }
}

private fun selectNextAvailableColor(
    availableColors: List<Color>,
    selectedColors: List<Color>,
    circularButtonIndex: Int
): Color {
    val selectedColor = selectedColors[circularButtonIndex]
    val remainingAvailableColors = selectedColors.filterNot { it in selectedColors }

    val dropWhile: List<Color> = availableColors.filterNot { it in remainingAvailableColors }
    val selectedColorIndex = dropWhile.indexOf(selectedColor)

    return if (selectedColorIndex + 1 < dropWhile.size) {
        dropWhile[selectedColorIndex + 1]
    } else {
        dropWhile[0]
    }
}

private fun checkColors(
    pickedColors: List<Color>,
    correctColors: List<Color>
): List<Color> {
    val selectionResults = mutableListOf(Color.White, Color.White, Color.White, Color.White)
    pickedColors.forEachIndexed { index, color ->
        if (correctColors.contains(color)) {
            if (correctColors[index] == color) selectionResults[index] = Color.Red
            else selectionResults[index] = Color.Yellow
        } else selectionResults[index] = Color.White
    }
    return selectionResults
}