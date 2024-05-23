package com.example.masterand.deprecated

import com.example.masterand.db.repository.PlayerRepository
import com.example.masterand.db.repository.PlayerScoreRepository
import com.example.masterand.db.repository.ScoreRepository

@Deprecated(message = "replaced by dagger hilt")
interface AppContainer {
    val playerRepository: PlayerRepository
    val scoreRepository: ScoreRepository
    val playerScoreRepository: PlayerScoreRepository
}