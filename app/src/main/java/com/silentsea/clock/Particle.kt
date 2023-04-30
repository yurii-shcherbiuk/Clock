package com.silentsea.clock

import androidx.compose.animation.core.EaseInExpo
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotateRad
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class Particle(
    private var angle: Float = 0f,
    var speed: Float = 0f,
    var dist: Float = 0f,
    var distFrac: Float = 0f,
    private var size: Float = 0f,
    private var isFilled: Boolean = false,
    private var isFlowing: Boolean? = false,
    val color: State<Color>,
    val type: ParticleType = ParticleType.NOISE,
    var init: Boolean = false
) {
    fun draw(
        scope: DrawScope,
    ) {
        val radius = scope.size.minDimension / 2f

        val handlesStart = 0.2f

        val center = Offset(0f, 0f)

        val alpha: Float =
            min(
                1f,
                max(
                    0f,
                    if (distFrac <= handlesStart)
                        EaseInExpo.transform(distFrac / handlesStart)
                    else
                        EaseInExpo.transform((1f - distFrac) / (handlesStart / 2f))
                )
            )

        if (isFilled && isFlowing!!) {
            val threshold = if (type == ParticleType.HOUR) 0.2f else 0.4f
            val flowAlpha = max(0f, (distFrac - threshold) / distFrac) * alpha
            val gradient = Brush.sweepGradient(
                0.8f to color.value.copy(alpha = 0f),
                1f to color.value.copy(alpha = flowAlpha),
                center = center
            )

            scope.rotateRad(
                radians = angle,
                pivot = center
            ) {
                drawCircle(
                    brush = gradient,
                    radius = dist * radius,
                    center = center,
                    style = Stroke(width = radius * this@Particle.size / 2f)
                )
            }
        }

        scope.drawCircle(
            color = color.value.copy(alpha = alpha),
            radius = radius * size,
            center = Offset(cos(angle) * dist * radius, sin(angle) * dist * radius),
            style = if (isFilled) Fill else Stroke(width = radius * size / 4f)
        )
    }

    fun randomize() {
        val calendar = Calendar.getInstance()

        val currentHour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
        val currentMinute = calendar.get(Calendar.MINUTE).toFloat()
        val currentSecond = calendar.get(Calendar.SECOND).toFloat()

        val currentHourRadians =
            (currentHour * 2f * PI.toFloat() / 12f) +
                    (currentMinute * 2f * PI.toFloat() / (12f * 60f)) +
                    (currentSecond * 2f * PI.toFloat() / (12f * 60f * 60f))
        val currentMinuteRadians =
            (currentMinute * 2f * PI.toFloat() / 60f) +
                    (currentSecond * 2f * PI.toFloat() / (60f * 60f))

        speed = Rnd.getFloat(0.002f, 0.003f)
        isFilled = Rnd.getBool()
        isFlowing = false

        when (type) {
            ParticleType.HOUR -> {
                angle = Rnd.getFloat(
                    currentHourRadians - PI.toFloat() / 72,
                    currentHourRadians + PI.toFloat() / 72
                )
                size = 0.02f
                isFlowing = Rnd.ratio() > 0.9f
            }

            ParticleType.MINUTE -> {
                angle = Rnd.getFloat(
                    currentMinuteRadians - PI.toFloat() / 72,
                    currentMinuteRadians + PI.toFloat() / 72
                )
                size = 0.016f
                isFlowing = Rnd.ratio() > 0.8f
            }

            ParticleType.NOISE -> {
                val am = currentMinuteRadians
                val ah = currentHourRadians % (2f * PI.toFloat())
                val d = PI.toFloat() / 18f

                do {
                    angle = Rnd.ratio() * 2f * PI.toFloat()
                } while (
                    Particles.isBetween(angle, am - d, am + d) ||
                    Particles.isBetween(angle, ah - d, ah + d)
                )
                size =
                    if (Rnd.ratio() > 0.8f) Rnd.getFloat(
                        0.003f,
                        0.006f
                    ) else Rnd.getFloat(
                        0.004f,
                        0.008f
                    )
            }
        }
    }
}
