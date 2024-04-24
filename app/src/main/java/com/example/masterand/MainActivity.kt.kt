package com.example.masterand

import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.masterand.ui.theme.MasterAndTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}

@Composable
fun GameScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        //.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val numberOfColors = 4
        val score = remember { mutableIntStateOf(0) }
        ScoreText(score)

        val randomColors = generateRandomColors(numberOfColors)
        val selectedColors = selectRandomColors(randomColors)
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(numberOfColors) {
                GameRow(selectedColors, randomColors, true, {}, {})
            }
        }

        StartOverButton()
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
    colors: List<Color>,
    callbackColors: List<Color>,
    isClickable: Boolean,
    onSelectColorClick: (circularButtonIndex: Int) -> Unit,
    onCheckClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SelectableColorsRow(colors, onSelectColorClick)
        IconButton(modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = colors[0],
                contentColor = MaterialTheme.colorScheme.onBackground
            ), onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "icon")
        }
        FeedbackCircles(colors)

    }

}

@Composable
fun SelectableColorsRow(colors: List<Color>, onClick: (circularButtonIndex: Int) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        items(colors.size) { circularButtonIndex ->
            CircularButton(
                onClick,
                colors[circularButtonIndex]
            )
        }
    }

}

@Composable
fun CircularButton(onClick: (circularButtonIndex: Int) -> Unit, color: Color) {
    Button(
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = { /*TODO*/ }) {

    }
}

@Composable
fun FeedbackCircles(colors: List<Color>) {
    Column {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(5.dp)
        ) {
            SmallCircle(color = colors[0])
            SmallCircle(color = colors[1])
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(5.dp)
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
fun StartOverButton() {

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
    colors: List<Color>,
    pickedColors: List<Color>,
    circularButtonIndex: Int
): Color {
    val availableColors = colors.filter { it !in pickedColors }
    val nextColorIndex = circularButtonIndex % availableColors.size

    return availableColors[nextColorIndex]

}

private fun selectRandomColors(availableColors: List<Color>): List<Color> {
    return availableColors.shuffled().take(4)
}

private fun checkColors(
    pickedColors: List<Color>,
    correctColors: List<Color>,
    missingColor: Color
): List<Color> {
    val feedbackColors = mutableListOf<Color>()
    val mutableCorrectColors = correctColors.toMutableList()

    for (i in pickedColors.indices) {
        val pickedColor = pickedColors[i]
        val correctColor = mutableCorrectColors[i]

        if (pickedColor == correctColor) {
            feedbackColors.add(Color.Red)
            mutableCorrectColors[i] = missingColor
        } else if (mutableCorrectColors.contains(pickedColor)) {
            feedbackColors.add(Color.Yellow)
            mutableCorrectColors[mutableCorrectColors.indexOf(pickedColor)] = missingColor
        } else {
            feedbackColors.add(missingColor)
        }
    }

    return feedbackColors
}