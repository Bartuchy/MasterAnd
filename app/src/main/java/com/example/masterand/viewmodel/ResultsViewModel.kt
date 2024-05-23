package com.example.masterand.viewmodel

import androidx.lifecycle.ViewModel
import com.example.masterand.db.broker.PlayerWithScore
import com.example.masterand.db.repository.PlayerScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(private val playerScoreRepository: PlayerScoreRepository) : ViewModel() {

    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>> =
        playerScoreRepository.loadPlayersWithScores()
}
