package com.example.masterand

//import android.content.Context
//import com.example.masterand.db.HighScoreDatabase
//import com.example.masterand.db.repository.PlayerRepository
//import com.example.masterand.db.repository.PlayerScoreRepository
//import com.example.masterand.db.repository.ScoreRepository
//import com.example.masterand.db.repository.impl.PlayerScoreRepositoryImpl
//import com.example.masterand.db.repository.impl.PlayerRepositoryImpl
//import com.example.masterand.db.repository.impl.ScoreRepositoryImpl
//
//class AppDataContainer(private val context: Context) : AppContainer {
//    override val playerRepository: PlayerRepository by lazy {
//        PlayerRepositoryImpl(HighScoreDatabase.getDatabase(context).playerDao())
//    }
//
//    override val scoreRepository: ScoreRepository by lazy {
//        ScoreRepositoryImpl(HighScoreDatabase.getDatabase(context).scoreDao())
//    }
//
//    override val playerScoreRepository: PlayerScoreRepository by lazy {
//        PlayerScoreRepositoryImpl(HighScoreDatabase.getDatabase(context).playerScoreDao())
//    }
//}