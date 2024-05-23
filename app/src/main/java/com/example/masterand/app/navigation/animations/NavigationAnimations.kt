package com.example.masterand.app.navigation.animations

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

class NavigationAnimations {
    fun enterTransitionAnimation() = fadeIn(animationSpec = tween(500)) +
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = EaseIn
                )
            )

    fun exitTransitionAnimation() = fadeOut(animationSpec = tween(500)) +
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = EaseOut
                )
            )
}