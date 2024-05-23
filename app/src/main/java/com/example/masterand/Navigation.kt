package com.example.masterand

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.masterand.app.game.GameScreen
import com.example.masterand.app.profile.ProfileScreen
import com.example.masterand.app.results.ResultsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "ProfileScreen") {
        composable(route = "ProfileScreen",
            enterTransition = {
                fadeIn(animationSpec = tween(500)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = EaseIn
                            )
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = EaseOut
                            )
                        )
            }) {
            ProfileScreen(onNavigateToGameScreen = { playerId, numberOfColors ->
                navController.navigate(
                    "GameScreen/$playerId/$numberOfColors"
                )
            })
        }
        composable(
            "GameScreen/{playerId}/{numberOfColors}",
            arguments = listOf(navArgument("numberOfColors") { type = NavType.StringType })
        ) { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId")!!.toLong()
            val numberOfColors = backStackEntry.arguments?.getString("numberOfColors")!!.toInt()

            GameScreen(
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                onNavigateToResultsScreen = { score -> navController.navigate("ResultsScreen/$playerId/$score/$numberOfColors") },
                numberOfColors = numberOfColors,
                playerId = playerId
            )

        }
        composable("ResultsScreen/{playerId}/{score}/{numberOfColors}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")!!.toInt()
            val numberOfColors = backStackEntry.arguments?.getString("numberOfColors")!!.toInt()
            val playerId = backStackEntry.arguments?.getString("playerId")!!.toLong()

            ResultsScreen(
                onNavigateToGameScreen = { navController.navigate("GameScreen/$playerId/$numberOfColors") },
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                score = score
            )
        }
    }

}