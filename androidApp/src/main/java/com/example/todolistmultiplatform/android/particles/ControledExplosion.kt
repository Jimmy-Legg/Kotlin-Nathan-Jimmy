package com.example.todolistmultiplatform.android.particles

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ControlledExplosion() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val transition1 = remember {
            MutableTransitionState(0f)
        }
        val progress1 by animateFloatAsState(
            targetValue = transition1.targetState,
            label = "explosion1",
            animationSpec = tween(durationMillis = 3000)
        )

        val transition2 = remember {
            MutableTransitionState(0f)
        }
        val progress2 by animateFloatAsState(
            targetValue = transition2.targetState,
            label = "explosion2",
            animationSpec = tween(durationMillis = 3000)
        )

        val transition3 = remember {
            MutableTransitionState(0f)
        }
        val progress3 by animateFloatAsState(
            targetValue = transition3.targetState,
            label = "explosion3",
            animationSpec = tween(durationMillis = 2500)
        )

        val transition4 = remember {
            MutableTransitionState(0f)
        }
        val progress4 by animateFloatAsState(
            targetValue = transition4.targetState,
            label = "explosion4",
            animationSpec = tween(durationMillis = 2500)
        )

        val transition5 = remember {
            MutableTransitionState(0f)
        }
        val progress5 by animateFloatAsState(
            targetValue = transition5.targetState,
            label = "explosion5",
            animationSpec = tween(durationMillis = 2000)
        )

        LaunchedEffect(key1 = true) {
            transition1.targetState = 1f
            delay(400)
            transition3.targetState = 1f
            delay(500)
            transition2.targetState = 1f
            delay(200)
            transition4.targetState = 1f
            delay(500)
            transition5.targetState = 1f
        }
        Box(
            modifier = Modifier
        ) {
            Explosion(progress1, 100, modifier = Modifier.offset(x = (75).dp, y = (150).dp))
            Explosion(progress2, 100, modifier = Modifier.offset(x = (-75).dp, y = (150).dp))
            Explosion(progress3, 75, modifier = Modifier.offset(x = (-50).dp, y = (50).dp))
            Explosion(progress4, 75, modifier = Modifier.offset(x = (50).dp, y = (50).dp))
            Explosion(progress5, 50, modifier = Modifier.offset(x = (0).dp, y = (0).dp))
        }

    }
}