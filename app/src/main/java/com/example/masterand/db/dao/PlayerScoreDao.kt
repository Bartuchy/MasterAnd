package com.example.masterand.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.masterand.db.broker.PlayerWithScore
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerScoreDao {
    @Query("select p.name playerName, s.value score from players p inner join scores s on p.playerId = s.playerId order by 2 asc")
    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>>
}