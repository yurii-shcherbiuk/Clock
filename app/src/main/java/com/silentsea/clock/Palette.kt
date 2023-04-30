package com.silentsea.clock

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.json.JSONArray
import java.util.Locale

class Palette {
    companion object {
        private var palettes: List<List<Color>>? = null

        fun loadPalettes(context: Context) {
            val json = context.assets.open("palettes.json").bufferedReader().use { it.readText() }

            val outerArray = JSONArray(json)
            val outerList = mutableListOf<List<Color>>()

            for (i in 0 until outerArray.length()) {
                val innerArray = outerArray.getJSONArray(i)
                val innerList = mutableListOf<Color>()

                for (j in 0 until innerArray.length()) {
                    val color = Color(
                        innerArray
                            .getString(j)
                            .replace("#", "")
                            .uppercase(Locale.ROOT)
                            .toLong(radix = 16)
                    )
                    innerList.add(color)
                }

                outerList.add(innerList.toList())
            }

            palettes = outerList.toList()
        }

        fun randomize(): List<Color> {
            return if (palettes != null) Rnd.getPalette(palettes!!) else listOf(
                Color.Black,
                Color.White,
                Color.White,
                Color.White
            )
        }

        fun lightDark(palette: List<Color>, dark: Boolean): List<Color> {
            return palette.sortedWith(
                if (dark)
                    compareBy { it.luminance() }
                else
                    compareByDescending { it.luminance() })
        }
    }
}