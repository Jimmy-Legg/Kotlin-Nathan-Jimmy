package com.example.todolistmultiplatform.android.particles

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.pow

class Particle(
    val color: Color,
    val startXPosition: Int,
    val startYPosition: Int,
    val maxHorizontalDisplacement: Float,
    val maxVerticalDisplacement: Float
) {
    val velocity = 4 * maxVerticalDisplacement
    val acceleration = -2 * velocity
    var currentXPosition = 0f
    var currentYPosition = 0f

    var visibilityThresholdLow = randomInRange(0f, 0.14f)
    var visibilityThresholdHigh = randomInRange(0f, 0.4f)

    val initialXDisplacement = 10.dp.toPx() * randomInRange(-1f, 1f)
    val initialYDisplacement = 10.dp.toPx() * randomInRange(-1f, 1f)

    var alpha = 0f
    var currentRadius = 0f
    val startRadius = 2.dp.toPx()
    val endRadius = if (randomBoolean(trueProbabilityPercentage = 20)) {
        randomInRange(startRadius, 10.dp.toPx())
    } else {
        randomInRange(1.5.dp.toPx(), startRadius)
    }

    fun updateProgress(explosionProgress: Float) {
        val trajectoryProgress =
            if (explosionProgress < visibilityThresholdLow || (explosionProgress > (1 - visibilityThresholdHigh))) {
                alpha = 0f; return
            } else (explosionProgress - visibilityThresholdLow).mapInRange(0f,1f - visibilityThresholdHigh - visibilityThresholdLow,0f, 1f)
        alpha = if (trajectoryProgress < 0.7f) 1f else (trajectoryProgress - 0.7f).mapInRange(
            0f,
            0.3f,
            1f,
            0f
        )
        currentRadius = startRadius + (endRadius - startRadius) * trajectoryProgress
        val currentTime = trajectoryProgress.mapInRange(0f, 1f, 0f, 1.4f)
        val verticalDisplacement =
            (currentTime * velocity + 0.5 * acceleration * currentTime.toDouble()
                .pow(2.0)).toFloat()
        currentYPosition = startXPosition + initialXDisplacement - verticalDisplacement
        currentXPosition =
            startYPosition + initialYDisplacement + maxHorizontalDisplacement * trajectoryProgress
    }
}