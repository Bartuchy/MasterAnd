package com.example.masterand.db.repository.module

import com.example.masterand.db.repository.PlayerScoreRepository
import com.example.masterand.db.repository.impl.PlayerScoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlayerScoreModule {
    @Binds
    abstract fun bindPlayerScoreRepository(playerScoreRepositoryImpl: PlayerScoreRepositoryImpl): PlayerScoreRepository
}