package com.example.masterand.db.repository

import com.example.masterand.db.entity.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getAllPlayersStream(): Flow<List<Player>>
    fun getPlayerStream(playerId: Long): Flow<Player?>
    suspend fun getPlayerByEmail(email: String): Player?
    suspend fun insertPlayer(player: Player) : Long
}