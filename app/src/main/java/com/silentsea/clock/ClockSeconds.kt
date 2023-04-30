package com.silentsea.clock

import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import java.util.Calendar
import kotlin.math.PI

class ClockSeconds(
    private val palette: List<State<Color>>
) {
    private fun radToDeg(rad: Float): Float = rad * 180f / PI.toFloat()

    fun draw(scope: DrawScope) {
        val secSize = PI.toFloat() / 50f

        val currentSecond = Calendar.getInstance().get(Calendar.SECOND)
            .toFloat() + System.currentTimeMillis() % 1000 / 1000f

        val timeAngle = currentSecond * (2f * PI.toFloat() / 60f) - secSize / 2f

        val radius = scope.size.minDimension / 2f
        val radiusInner = radius * 0.1f
        val radiusOuter = radius * 0.85f

        scope.drawArc(
            color = palette[3].value.copy(alpha = 0.15f),
            startAngle = radToDeg(0f),
            sweepAngle = radToDeg(PI.toFloat() * 2f),
            useCenter = false,
            topLeft = Offset(-radiusOuter, -radiusOuter),
            size = Size(radiusOuter * 2f, radiusOuter * 2f),
            style = Stroke(width = radius * 0.004f, cap = StrokeCap.Round)
        )

        scope.drawArc(
            color = palette[3].value.copy(alpha = 0.15f),
            startAngle = radToDeg(0f),
            sweepAngle = radToDeg(PI.toFloat() * 2f),
            useCenter = false,
            topLeft = Offset(-radiusInner, -radiusInner),
            size = Size(radiusInner * 2f, radiusInner * 2f),
            style = Stroke(width = radius * 0.004f, cap = StrokeCap.Round)
        )

        scope.drawArc(
            color = palette[3].value,
            startAngle = radToDeg(timeAngle),
            sweepAngle = radToDeg(secSize),
            useCenter = false,
            topLeft = Offset(-radiusOuter, -radiusOuter),
            size = Size(radiusOuter * 2f, radiusOuter * 2f),
            style = Stroke(width = radius * 0.02f, cap = StrokeCap.Round)
        )

        scope.drawArc(
            color = palette[3].value,
            startAngle = radToDeg(timeAngle - (secSize * 6f)),
            sweepAngle = radToDeg(secSize * 12f),
            useCenter = false,
            topLeft = Offset(-radiusInner, -radiusInner),
            size = Size(radiusInner * 2f, radiusInner * 2f),
            style = Stroke(width = radius * 0.02f, cap = StrokeCap.Round)
        )
    }
}