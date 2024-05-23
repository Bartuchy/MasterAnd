package com.example.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.masterand.ui.theme.MasterAndTheme
import com.example.masterand.viewmodel.AppViewModelProvider
import com.example.masterand.viewmodel.ResultsViewModel

class ResultsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultsScreen(
                        onNavigateToProfileScreen = {},
                        onNavigateToGameScreen = {},
                        score = 5
                    )
                }
            }
        }
    }
}

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToGameScreen: () -> Unit,
    score: Int
) {
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


        val playersWithScore =
            viewModel.loadPlayersWithScores().collectAsState(initial = emptyList())

        var scoreBoardSize = 5
        if (playersWithScore.value.size < 5) {
            scoreBoardSize = playersWithScore.value.size
        }

        LazyColumn(
            Modifier.heightIn(min = 200.dp, max = 400.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(scoreBoardSize) { scoreIndex ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = playersWithScore.value[scoreIndex].playerName,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "${playersWithScore.value[scoreIndex].score}",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                    Divider(color= Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onNavigateToGameScreen() }) {
            Text(text = "Restart game")
        }
        Button(onClick = { onNavigateToProfileScreen() }) {
            Text(text = "Logout")
        }
    }
}