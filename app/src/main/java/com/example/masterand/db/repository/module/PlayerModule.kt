package com.example.masterand.db.repository.module

import com.example.masterand.db.repository.PlayerRepository
import com.example.masterand.db.repository.impl.PlayerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlayerModule {
    @Binds
    abstract fun bindPlayerRepository(playerRepositoryImpl: PlayerRepositoryImpl): PlayerRepository
}