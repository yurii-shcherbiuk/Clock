package com.silentsea.clock

import androidx.compose.ui.graphics.Color
import java.util.Calendar
import kotlin.random.Random

class Rnd {
    companion object {
        private var seed: Long = Calendar.getInstance().timeInMillis
            set(v) {
                field = v
                random = Random(v)
            }
        private var random: Random = Random(seed)

        fun ratio(): Float = random.nextFloat()

        fun getInt(min: Int, max: Int): Int {
            return min + random.nextInt(max + 1 - min)
        }

        fun getFloat(min: Float, max: Float): Float {
            return min + random.nextFloat() * (max - min)
        }

        fun getBool(): Boolean = random.nextFloat() < 0.5f

        private fun shuffle(list: List<Color>): List<Color> {
            val temp: MutableList<Color> = list.toMutableList()
            for (i in 0 until temp.size) {
                val j: Int = random.nextInt(temp.size)
                if (j == i) {
                    continue
                }
                val item = temp[j]
                temp[j] = temp[i]
                temp[i] = item
            }
            return temp.toList()
        }

        private fun getItem(list: List<List<Color>>): List<Color> = list[random.nextInt(list.size)]

        fun getPalette(palettes: List<List<Color>>): List<Color> {
            val palette: List<Color> = getItem(palettes)
            return shuffle(palette)
        }
    }
}
