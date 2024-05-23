package com.example.masterand.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.masterand.db.dao.PlayerDao
import com.example.masterand.db.dao.PlayerScoreDao
import com.example.masterand.db.dao.ScoreDao
import com.example.masterand.db.entity.Player
import com.example.masterand.db.entity.Score

@Database(
    entities = [Score::class, Player::class],
    version = 1,
    exportSchema = false
)
abstract class HighScoreDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun scoreDao(): ScoreDao
    abstract fun playerScoreDao(): PlayerScoreDao

    companion object {
        @Volatile
        private var Instance: HighScoreDatabase? = null

        fun getDatabase(context: Context): HighScoreDatabase {
            return Room.databaseBuilder(
                context,
                HighScoreDatabase::class.java,
                "highscore_database2"
            )
                .build()
                .also { Instance = it }
        }
    }
}