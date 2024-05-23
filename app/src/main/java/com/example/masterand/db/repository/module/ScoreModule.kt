package com.example.masterand.db.repository.module

import com.example.masterand.db.repository.ScoreRepository
import com.example.masterand.db.repository.impl.ScoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ScoreModule {
    @Binds
    abstract fun bindScoreRepository(scoresRepositoryImpl: ScoreRepositoryImpl): ScoreRepository
}
