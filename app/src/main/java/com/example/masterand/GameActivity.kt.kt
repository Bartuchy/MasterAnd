package com.example.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.masterand.ui.theme.MasterAndTheme
import kotlin.random.Random


class GameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen(onNavigateToProfileScreen = {}, onNavigateToResultsScreen = {}, 5)
                }
            }
        }
    }
}

@Composable
fun GameScreen(
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToResultsScreen: (score: Int) -> Unit,
    numberOfColors: Int
) {
    val availableColors = generateRandomColors(numberOfColors)
    val correctColors = selectRandomColors(availableColors)


    val data = remember {
        mutableStateListOf(RowData(1))
    }
    val score = remember {
        mutableIntStateOf(1)
    }

    val isWon = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ScoreText(score)

        LazyColumn(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(items = data) { row ->

                val appearState = remember(row.id) { mutableStateOf(false) }
                if (row.id == data.last().id) {
                    LaunchedEffect(key1 = row.id) {
                        appearState.value = true
                    }
                }

                AnimatedVisibility(
                    visible = appearState.value,
                    enter = expandVertically(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing
                        )
                    )
                ) {
                    GameRow(
                        selectedColors = row.selectedColors,
                        callbackColors = row.callbackColors,
                        isClickable = row.isClickable,
                        onSelectColorClick = { index ->
                            row.selectedColors[index] = selectNextAvailableColor(
                                availableColors = availableColors,
                                selectedColors = row.selectedColors,
                                circularButtonIndex = index
                            )
                        },
                        onCheckClick = {
                            row.callbackColors.clear()
                            row.callbackColors.addAll(
                                checkColors(
                                    pickedColors = row.selectedColors,
                                    correctColors = correctColors
                                )
                            )
                            row.isClickable = false
                            if (correctColors != row.selectedColors) {
                                data.add(RowData(row.id + 1))
                                score.intValue++
                            } else {
                                isWon.value = true
                            }
                        })
                }
            }
        }

        if (isWon.value) {
            HighScoreButtonButton(
                onNavigateToResultsScreen = onNavigateToResultsScreen,
                score = score.intValue
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
    onCheckClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SelectableColorsRow(selectedColors, onSelectColorClick)
        Spacer(modifier = Modifier.padding(2.dp))

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
    Button(
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = {
            onClick()
        }) {

    }
}

@Composable
fun FeedbackCircles(colors: List<Color>) {
    Column {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(2.dp)
        ) {
            SmallCircle(color = colors[0])
            SmallCircle(color = colors[1])
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(2.dp)
        ) {
            SmallCircle(color = colors[2])
            SmallCircle(color = colors[3])
        }
    }
}

@Composable
fun SmallCircle(color: Color) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .size(20.dp)
    )
    Spacer(modifier = Modifier.padding(2.dp))
}

@Composable
fun HighScoreButtonButton(onNavigateToResultsScreen: (score: Int) -> Unit, score: Int) {
    println(score)
    Button(modifier = Modifier
        .padding(16.dp)
        .wrapContentHeight(Alignment.Top), onClick = { onNavigateToResultsScreen(score) }) {
        Text("High score")
    }
}

@Composable
fun LogoutButton(onNavigateToProfileScreen: () -> Unit) {
    Button(modifier = Modifier.padding(16.dp), onClick = { onNavigateToProfileScreen() }) {
        Text(text = "Logout")
    }
}

private fun generateRandomColors(numberOfColors: Int): List<Color> {
    val randomColors = mutableListOf<Color>()

    repeat(numberOfColors) {
        val red = Random.nextFloat()
        val green = Random.nextFloat()
        val blue = Random.nextFloat()

        val color = Color(red, green, blue)
        randomColors.add(color)
    }

    return randomColors
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

private fun selectRandomColors(availableColors: List<Color>): List<Color> {
    return availableColors.shuffled().take(4)
}

private fun checkColors(
    pickedColors: List<Color>,
    correctColors: List<Color>,
    //missingColor: Color
): List<Color> {
//    val feedbackColors = mutableListOf<Color>()
//    val mutableCorrectColors = correctColors.toMutableList()
//
//    for (i in pickedColors.indices) {
//        val pickedColor = pickedColors[i]
//        val correctColor = mutableCorrectColors[i]
//
//        if (pickedColor == correctColor) {
//            feedbackColors.add(Color.Red)
//            mutableCorrectColors[i] = missingColor
//        } else if (mutableCorrectColors.contains(pickedColor)) {
//            feedbackColors.add(Color.Yellow)
//            mutableCorrectColors[mutableCorrectColors.indexOf(pickedColor)] = missingColor
//        } else {
//            feedbackColors.add(missingColor)
//        }
//    }
//
//    return feedbackColors
    val selectionResults = mutableListOf(Color.White, Color.White, Color.White, Color.White)
    pickedColors.forEachIndexed { index, color ->
        if (correctColors.contains(color)) {
            if (correctColors[index] == color) selectionResults[index] = Color.Red
            else selectionResults[index] = Color.Yellow
        } else selectionResults[index] = Color.White
    }
    return selectionResults
}