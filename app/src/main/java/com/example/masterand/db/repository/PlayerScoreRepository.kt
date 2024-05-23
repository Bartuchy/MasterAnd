package com.example.masterand.db.repository

import com.example.masterand.db.broker.PlayerWithScore
import kotlinx.coroutines.flow.Flow

interface PlayerScoreRepository {
    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>>
}