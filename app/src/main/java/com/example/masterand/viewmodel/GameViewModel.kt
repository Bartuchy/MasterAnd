package com.example.masterand.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.masterand.RowData
import com.example.masterand.db.entity.Score
import com.example.masterand.db.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(private val scoreRepository: ScoreRepository) : ViewModel() {

    var numberOfColors: Int = 5
    var playerId: Long = 0L

    val availableColors = generateRandomColors(numberOfColors)
    val correctColors = selectRandomColors(availableColors)


    val data = mutableStateListOf(RowData(1))
    val score = mutableIntStateOf(1)
    val isWon = mutableStateOf(false)


    suspend fun saveNewScore() {
        val scoreEntity = Score(playerId = playerId, value = score.intValue)
        scoreRepository.insertScore(scoreEntity)
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

    private fun selectRandomColors(availableColors: List<Color>): List<Color> {
        return availableColors.shuffled().take(4)
    }
}