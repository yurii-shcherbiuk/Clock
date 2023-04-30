package com.silentsea.clock

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

class Particles(
    numHour: Int = 100,
    numMinute: Int = 100,
    numNoise: Int = 5000,
    palette: List<State<Color>>
) {
    private val particles: List<Particle> =
        (0 until numHour).map {
            Particle(
                type = ParticleType.HOUR,
                color = palette[3]
            )
        } + (0 until numMinute).map {
            Particle(
                type = ParticleType.MINUTE,
                color = palette[3]
            )
        } + (0 until numNoise).map {
            Particle(
                type = ParticleType.NOISE,
                color = palette[Rnd.getInt(1, 3)]
            )
        }

    fun draw(
        coefficient: Float,
        scope: DrawScope,
    ) {
        particles.forEach { particle ->
            particle.dist += particle.speed * coefficient

            if (!particle.init) {
                particle.dist = Rnd.getFloat(0f, 1f)
                particle.randomize()
                particle.init = true
            }

            if (particle.dist > 1f) {
                particle.dist = 0f
                particle.randomize()
            }

            particle.distFrac =
                particle.dist / (when (particle.type) {
                    ParticleType.HOUR -> 0.55f
                    ParticleType.MINUTE -> 0.75f
                    else -> 0.85f
                })

            particle.draw(scope)
        }
    }

    companion object {
        fun isBetween(value: Float, min: Float, max: Float): Boolean = value in min..max
    }
}