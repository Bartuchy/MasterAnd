package com.example.masterand.app.game.helpers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.masterand.app.game.GameViewModel

data class GeneratedRowData(var id: Int) {
    var selectedColors = mutableStateListOf(Color.White, Color.White, Color.White, Color.White)
    var callbackColors = mutableStateListOf(Color.White, Color.White, Color.White, Color.White)
    var isClickable by mutableStateOf(true)

    private fun selectNextAvailableColor(
        availableColors: List<Color>,
        selectedColors: List<Color>,
        circularButtonIndex: Int
    ): Color {
        val selectedColor = selectedColors[circularButtonIndex]

        //val trulySelectedColors = selectedColors.filter { it != selectedColor }
        //val remainingAvailableColors: List<Color> = availableColors.filterNot { it in trulySelectedColors }

        val selectedColorIndex = availableColors.indexOf(selectedColor)

        return if (selectedColorIndex + 1 < availableColors.size) {
            availableColors[selectedColorIndex + 1]
        } else {
            availableColors[0]
        }
    }

    private fun checkColors(
        selectedColors: List<Color>,
        correctColors: List<Color>
    ): List<Color> {
        val selectionResults = mutableListOf(Color.White, Color.White, Color.White, Color.White)

        selectedColors.forEachIndexed { index, color ->
            if (correctColors.contains(color)) {
                if (correctColors[index] == color) selectionResults[index] = Color.Red
                else selectionResults[index] = Color.Yellow
            } else selectionResults[index] = Color.White
        }

        return selectionResults
    }

    fun onSelectColorClick(index: Int, viewModel: GameViewModel) {
        selectedColors[index] = selectNextAvailableColor(
            availableColors = viewModel.availableColors,
            selectedColors = selectedColors,
            circularButtonIndex = index
        )
    }

    fun onCheckClick(viewModel: GameViewModel) {
        callbackColors.clear()
        callbackColors.addAll(
            checkColors(
                selectedColors = selectedColors,
                correctColors = viewModel.correctColors
            )
        )
        isClickable = false
        if (viewModel.correctColors != selectedColors) {
            viewModel.generatedRowData.add(GeneratedRowData(id + 1))
            viewModel.score.intValue++
        } else {
            viewModel.isWon.value = true
        }
    }
}