package com.example.masterand.db.repository.impl

import com.example.masterand.db.dao.PlayerDao
import com.example.masterand.db.entity.Player
import com.example.masterand.db.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class PlayersRepositoryImpl(private val playerDao: PlayerDao) : PlayerRepository {
    override fun getAllPlayersStream(): Flow<List<Player>> {
        TODO("Not yet implemented")
    }

    override fun getPlayerStream(playerId: Long): Flow<Player?> =
        playerDao.getPlayerStream(playerId)
    override suspend fun getPlayerByEmail(email: String): Player? =
        playerDao.getPlayerByEmail(email)
    override suspend fun insertPlayer(player: Player): Long = playerDao.insert(player)
}