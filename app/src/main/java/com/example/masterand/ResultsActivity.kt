package com.example.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.masterand.ui.theme.MasterAndTheme

class ResultsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultsScreen({}, {}, 5)
                }
            }
        }
    }
}

@Composable
fun ResultsScreen(onNavigateToProfileScreen: () -> Unit, onNavigateToGameScreen: () -> Unit, score: Int) {
    println("results $score")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Results",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(bottom = 48.dp)
        )
        Text(
            text = "Recent score: $score",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(bottom = 48.dp)
        )
        Button(onClick = { onNavigateToGameScreen() }) {
            Text(text = "Restart game")
        }
        Button(onClick = { onNavigateToProfileScreen() }) {
            Text(text = "Logout")
        }
    }
}