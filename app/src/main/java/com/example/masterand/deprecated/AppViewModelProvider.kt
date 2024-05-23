package com.example.masterand.deprecated

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.masterand.app.game.GameViewModel
import com.example.masterand.app.profile.ProfileViewModel
import com.example.masterand.app.results.ResultsViewModel

@Deprecated(message = "replaced by dagger hilt")
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ProfileViewModel(masterAndApplication().container.playerRepository)
        }

        initializer {
            GameViewModel(masterAndApplication().container.scoreRepository)
        }

        initializer {
            ResultsViewModel(masterAndApplication().container.playerScoreRepository)
        }
    }
}

@Deprecated(message = "replaced by dagger hilt")
fun CreationExtras.masterAndApplication(): MasterAndApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MasterAndApplication)
