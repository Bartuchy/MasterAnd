package com.example.masterand.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.masterand.db.entity.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Upsert
    suspend fun insert(player: Player): Long

    @Query("Select * from players where playerId = :playerId")
    fun getPlayerStream(playerId: Long): Flow<Player>

    @Query("Select * from players where email = :email")
    suspend fun getPlayerByEmail(email: String): Player?
}