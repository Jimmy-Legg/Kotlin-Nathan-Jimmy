package com.example.todolistmultiplatform.android.particles

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import java.util.Random

fun Float.mapInRange(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return outMin + (((this - inMin) / (inMax - inMin)) * (outMax - outMin))
}

fun Int.dpToPx() = toFloat().dpToPx()
fun Dp.toPx() = value.dpToPx()

fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density


private val random = Random()
fun Float.randomTillZero() = this * random.nextFloat()
fun randomInRange(min:Float,max:Float) = min + (max - min).randomTillZero()
fun randomBoolean(trueProbabilityPercentage: Int) = random.nextFloat() < trueProbabilityPercentage/100f