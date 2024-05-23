package com.example.masterand.db.repository.impl

import com.example.masterand.db.dao.ScoreDao
import com.example.masterand.db.entity.Score
import com.example.masterand.db.repository.ScoreRepository

class ScoreRepositoryImpl(private val scoreDao: ScoreDao) : ScoreRepository {
    override suspend fun insertScore(score: Score) {
        scoreDao.insertScore(score)
    }
}