package com.example.masterand.db.repository.impl

import com.example.masterand.db.broker.PlayerWithScore
import com.example.masterand.db.dao.PlayerScoreDao
import com.example.masterand.db.repository.PlayerScoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlayerScoreRepositoryImpl @Inject constructor(private val playerScoreDao: PlayerScoreDao) :
    PlayerScoreRepository {
    override fun loadPlayersWithScores(): Flow<List<PlayerWithScore>> =
        playerScoreDao.loadPlayersWithScores()

}