package com.example.masterand

import com.example.masterand.db.repository.PlayerRepository
import com.example.masterand.db.repository.PlayerScoreRepository
import com.example.masterand.db.repository.ScoreRepository

interface AppContainer {
    val playerRepository: PlayerRepository
    val scoreRepository: ScoreRepository
    val playerScoreRepository: PlayerScoreRepository
}