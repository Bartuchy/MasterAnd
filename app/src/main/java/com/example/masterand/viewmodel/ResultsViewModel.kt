package com.example.masterand.viewmodel

import androidx.lifecycle.ViewModel
import com.example.masterand.db.broker.PlayerWithScore
import com.example.masterand.db.repository.PlayerScoreRepository
import kotlinx.coroutines.flow.Flow

class ResultsViewModel(private val playerScoreRepository: PlayerScoreRepository) : ViewModel() {

    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>> =
        playerScoreRepository.loadPlayersWithScores()
}
