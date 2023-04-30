package com.silentsea.clock

import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class ClockFace(
    private val arms: Int = 60,
    private val palette: List<State<Color>>
) {
    private val quarters: Int = floor((arms / 12).toDouble()).toInt()

    fun draw(scope: DrawScope) {
        val radius = scope.size.minDimension / 2f * 0.95f
        val circleRadius = radius * 0.012f

        for (i in 1..arms) {
            val angle = i * 360f / arms
            val a = -angle * PI.toFloat() / 180f
            val point = Offset(radius * cos(a), radius * sin(a))

            if (i % quarters == 0) {
                scope.drawCircle(
                    color = palette[3].value,
                    radius = circleRadius * 2f,
                    center = point,
                    style = Fill
                )
            } else {
                scope.drawCircle(
                    color = palette[3].value.copy(alpha = 0.4f),
                    radius = circleRadius,
                    center = point,
                    style = Stroke(width = circleRadius * 0.5f)
                )
            }
        }
    }
}