package com.example.todolistmultiplatform.android.particles

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Explosion(progress: Float, particlesAmount: Int, modifier: Modifier) {
    val sizeDp = 350.dp
    val sizePx = sizeDp.toPx()
    val sizePxHalf = sizePx / 2
    val particles = remember {
        List(particlesAmount) {
            Particle(
                color = Color(listOf(0xffea4335, 0xff4285f4, 0xfffbbc05, 0xff34a853).random()),
                startXPosition = sizePxHalf.toInt(),
                startYPosition = sizePxHalf.toInt(),
                maxHorizontalDisplacement = sizePx * randomInRange(-0.9f, 0.9f),
                maxVerticalDisplacement = sizePx * randomInRange(0.2f, 0.38f)
            )
        }
    }
    particles.forEach {
        it.updateProgress(progress)

    }
    Log.d("progress", progress.toString())
    Canvas(
        modifier = modifier
            .size(sizeDp)
    ) {
        particles.forEach { particle ->
            drawCircle(
                alpha = particle.alpha,
                color = particle.color,
                radius = particle.currentRadius,
                center = Offset(particle.currentXPosition, particle.currentYPosition),
            )
        }
    }
}
