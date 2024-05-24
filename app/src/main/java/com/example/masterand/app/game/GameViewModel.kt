package com.example.masterand.app.game

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.masterand.app.game.helpers.GeneratedRowData
import com.example.masterand.db.entity.Score
import com.example.masterand.db.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(private val scoreRepository: ScoreRepository) :
    ViewModel() {
    var playerId: Long = 0L

    var availableColors = mutableListOf<Color>()
    var correctColors: List<Color> = ArrayList()


    val generatedRowData = mutableStateListOf(GeneratedRowData(1))
    val score = mutableIntStateOf(1)
    val isWon = mutableStateOf(false)


    suspend fun saveNewScore() {
        val scoreEntity = Score(playerId = playerId, value = score.intValue)
        scoreRepository.insertScore(scoreEntity)
    }

    fun init(numberOfColors: Int, playerId: Long) {
        availableColors = generateRandomColors(numberOfColors)
        correctColors = selectRandomColors(availableColors)
        this.playerId = playerId
    }

    private fun generateRandomColors(numberOfColors: Int): MutableList<Color> {
        val randomColors = mutableListOf<Color>()

        repeat(numberOfColors) {
            val red = Random.nextFloat()
            val green = Random.nextFloat()
            val blue = Random.nextFloat()

            val color = Color(red, green, blue)
            randomColors.add(color)
        }

        Log.i("generated colors number", randomColors.size.toString())
        return randomColors
    }

    private fun selectRandomColors(availableColors: MutableList<Color>): List<Color> {
        return availableColors.shuffled().take(4)
    }
}