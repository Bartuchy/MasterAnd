package com.example.masterand.db.repository

import com.example.masterand.db.entity.Score

interface ScoreRepository {
    suspend fun insertScore(score: Score)
}