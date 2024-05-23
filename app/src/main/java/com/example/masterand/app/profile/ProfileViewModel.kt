package com.example.masterand.app.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.masterand.db.entity.Player
import com.example.masterand.db.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val playersRepository: PlayerRepository) : ViewModel() {
    var playerId = mutableLongStateOf(0L)
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val numberOfColors = mutableStateOf("")
    var profileImageUri = mutableStateOf<Uri?>(null)
    val errors = mutableStateMapOf(
        Pair("name", false), Pair("email", false), Pair("numberOfColors", false)
    )


    suspend fun savePlayer(): Long {
        var player: Player? = playersRepository.getPlayerByEmail(email.value)
        if (player != null) {
            player.name = name.value
            player.email = email.value
            player.profileImageUri = profileImageUri.value.toString()
        } else {
            player = Player(
                name = name.value,
                email = email.value,
                profileImageUri = profileImageUri.value.toString()
            )
        }

        Log.i("insert: ", player.name + " " + player.email + " " + player.profileImageUri)
        return playersRepository.insertPlayer(player)
    }

    suspend fun getPlayerByEmail(): Player? {
        return playersRepository.getPlayerByEmail(email.value)
    }
}
