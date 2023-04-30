package com.silentsea.clock

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive

@Composable
fun Scene() {
    val context = LocalContext.current

    Palette.loadPalettes(context)

    var dark by remember {
        mutableStateOf(false)
    }

    var monet by remember {
        mutableStateOf(false)
    }

    var palette by remember {
        mutableStateOf(
            Palette.randomize()
        )
    }

    val currentPalette by remember(monet, dark, palette) {
        mutableStateOf(
            if (monet) {
                val darkColorScheme = dynamicDarkColorScheme(context)
                val lightColorScheme = dynamicLightColorScheme(context)
                if (dark)
                    listOf(
                        darkColorScheme.background,
                        darkColorScheme.secondary,
                        darkColorScheme.tertiary,
                        darkColorScheme.primary
                    )
                else
                    listOf(
                        lightColorScheme.background,
                        lightColorScheme.secondary,
                        lightColorScheme.tertiary,
                        lightColorScheme.primary
                    )
            } else
                Palette.lightDark(palette, dark)
        )
    }

    val spec: AnimationSpec<Color> = tween(durationMillis = 300, easing = EaseInOutSine)

    val animatedPalette = listOf(
        animateColorAsState(
            targetValue = currentPalette[0],
            animationSpec = spec
        ),
        animateColorAsState(
            targetValue = currentPalette[1],
            animationSpec = spec
        ),
        animateColorAsState(
            targetValue = currentPalette[2],
            animationSpec = spec
        ),
        animateColorAsState(
            targetValue = currentPalette[3],
            animationSpec = spec
        ),
    )

    val coefficient: MutableState<Float> = remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(Unit) {
        var lastFrame = 0L
        while (isActive) {
            val nextFrame = awaitFrame()
            if (lastFrame != 0L) {
                val fps = 1_000_000_000F / (nextFrame - lastFrame).toFloat()
                coefficient.value = 60f / fps
            }
            lastFrame = nextFrame
        }
    }

    val particles = remember {
        Particles(palette = animatedPalette)
    }

    val clockFace = remember {
        ClockFace(palette = animatedPalette)
    }

    val clockSeconds = remember {
        ClockSeconds(palette = animatedPalette)
    }

    val bars = rememberSystemUiController()

    bars.setSystemBarsColor(color = animatedPalette[0].value)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedPalette[0].value)
            .padding(25.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp, 100.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .width(220.dp)
                    .padding(0.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = context.getString(R.string.dark_theme),
                    color = animatedPalette[3].value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 1.sp
                )
                Switch(
                    checked = dark,
                    onCheckedChange = {
                        dark = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = animatedPalette[3].value,
                        checkedTrackColor = animatedPalette[3].value.copy(alpha = 0.3f),
                        checkedBorderColor = animatedPalette[1].value,
                        uncheckedThumbColor = animatedPalette[2].value,
                        uncheckedTrackColor = animatedPalette[2].value.copy(alpha = 0.3f),
                        uncheckedBorderColor = animatedPalette[1].value,
                        disabledCheckedThumbColor = animatedPalette[3].value.copy(alpha = 0.38f),
                        disabledCheckedTrackColor = animatedPalette[1].value.copy(alpha = 0.38f),
                        disabledUncheckedThumbColor = animatedPalette[3].value.copy(alpha = 0.38f),
                        disabledUncheckedTrackColor = animatedPalette[1].value.copy(alpha = 0.38f),
                    )
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Row(
                    modifier = Modifier
                        .width(220.dp)
                        .padding(0.dp, 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = context.getString(R.string.monet_theme),
                        color = animatedPalette[3].value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 1.sp
                    )
                    Switch(
                        checked = monet,
                        onCheckedChange = {
                            monet = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = animatedPalette[3].value,
                            checkedTrackColor = animatedPalette[3].value.copy(alpha = 0.3f),
                            checkedBorderColor = animatedPalette[1].value,
                            uncheckedThumbColor = animatedPalette[2].value,
                            uncheckedTrackColor = animatedPalette[2].value.copy(alpha = 0.3f),
                            uncheckedBorderColor = animatedPalette[1].value,
                            disabledCheckedThumbColor = animatedPalette[3].value.copy(alpha = 0.38f),
                            disabledCheckedTrackColor = animatedPalette[1].value.copy(alpha = 0.38f),
                            disabledUncheckedThumbColor = animatedPalette[3].value.copy(alpha = 0.38f),
                            disabledUncheckedTrackColor = animatedPalette[1].value.copy(alpha = 0.38f),
                        )
                    )
                }
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (!monet) palette = Palette.randomize()
                        }
                    )
                }
        ) {
            withTransform({
                rotate(-90f)
                translate(size.width / 2f, size.height / 2f)
            }) {
                particles.draw(coefficient.value, this)
                clockFace.draw(this)
                clockSeconds.draw(this)
            }
        }
    }
}