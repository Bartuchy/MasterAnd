package com.example.masterand.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.masterand.db.entity.Score

@Dao
interface ScoreDao {
    @Insert
    suspend fun insertScore(score: Score)
}