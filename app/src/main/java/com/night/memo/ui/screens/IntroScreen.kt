package com.night.memo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.night.memo.ui.theme.CrimsonRed
import com.night.memo.ui.theme.DeepIndigo
import com.night.memo.ui.theme.GlassCard
import com.night.memo.ui.theme.MidnightBlue
import com.night.memo.ui.theme.RoyalPurple
import com.night.memo.ui.theme.SoftPurple
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreen(
    onGetStarted: () -> Unit,
    onOpenLetter: () -> Unit,
    modifier: Modifier = Modifier
) {
    var letterOpened by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    // Beating heart animation
    val infiniteTransition = rememberInfiniteTransition(label = "heart")
    val heartScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartBeat"
    )

    // Envelope float animation
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    // Envelope glow pulse
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "envelopeGlow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MidnightBlue, DeepIndigo, RoyalPurple, SoftPurple)
                )
            )
            .systemBarsPadding()
            .combinedClickable(
                onClick = {
                    if (!letterOpened) {
                        onGetStarted()
                    }
                },
                onLongClick = {
                    if (!letterOpened) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        letterOpened = true
                    }
                }
            )
    ) {
        // Star particles background
        StarParticles()

        // Aurora light spots
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = SoftPurple.copy(alpha = 0.12f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.8f, size.height * 0.2f)
            )
            drawCircle(
                color = Color(0xFF6A4ABE).copy(alpha = 0.08f),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.2f, size.height * 0.8f)
            )
        }

        if (!letterOpened) {
            // === ENVELOPE VIEW ===
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // Envelope with glow
                Box(
                    modifier = Modifier
                        .size(220.dp, 160.dp)
                        .scale(1f + floatOffset / 400f),
                    contentAlignment = Alignment.Center
                ) {
                    // Soft glow behind envelope
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color(0xFFFF6B81).copy(alpha = glowAlpha * 0.15f),
                            radius = size.width * 0.7f,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    }

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val cornerR = 12.dp.toPx()

                        // Shadow
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.2f),
                            topLeft = Offset(3.dp.toPx(), 4.dp.toPx()),
                            size = Size(w, h),
                            cornerRadius = CornerRadius(cornerR)
                        )

                        // Envelope body
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset.Zero,
                            size = Size(w, h),
                            cornerRadius = CornerRadius(cornerR)
                        )

                        // Envelope flap
                        val flap = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(w / 2, h * 0.38f)
                            lineTo(w, 0f)
                            close()
                        }
                        clipPath(flap) {
                            drawRoundRect(
                                color = Color.White.copy(alpha = 0.85f),
                                topLeft = Offset.Zero,
                                size = Size(w, h),
                                cornerRadius = CornerRadius(cornerR)
                            )
                        }

                        // Flap border
                        val flapLine = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(w / 2, h * 0.38f)
                            lineTo(w, 0f)
                        }
                        drawPath(flapLine, color = Color(0xFFE0E0E0), style = Stroke(1.dp.toPx()))

                        // Red ribbon
                        drawLine(
                            color = CrimsonRed,
                            start = Offset(0f, h / 2),
                            end = Offset(w, h / 2),
                            strokeWidth = 3.dp.toPx()
                        )

                        // Ribbon bow center
                        drawCircle(
                            color = CrimsonRed,
                            radius = 7.dp.toPx(),
                            center = Offset(w / 2, h / 2)
                        )

                        // Ribbon left loop
                        drawCircle(
                            color = CrimsonRed.copy(alpha = 0.8f),
                            radius = 10.dp.toPx(),
                            center = Offset(w / 2 - 14.dp.toPx(), h / 2 - 2.dp.toPx())
                        )

                        // Ribbon right loop
                        drawCircle(
                            color = CrimsonRed.copy(alpha = 0.8f),
                            radius = 10.dp.toPx(),
                            center = Offset(w / 2 + 14.dp.toPx(), h / 2 - 2.dp.toPx())
                        )

                        // Envelope border
                        drawRoundRect(
                            color = Color(0xFFE0E0E0),
                            topLeft = Offset.Zero,
                            size = Size(w, h),
                            cornerRadius = CornerRadius(cornerR),
                            style = Stroke(1.dp.toPx())
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Hint text
                Text(
                    text = "长按开启",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Beating heart
                Text(
                    text = "\u2665",
                    fontSize = 24.sp,
                    color = Color(0xFFFF6B81),
                    modifier = Modifier.scale(heartScale)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Bottom hint
                Text(
                    text = "轻触进入备忘录",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        } else {
            // === LETTER VIEW (with staggered fade-in) ===
            LetterView(onOpenLetter = onOpenLetter)
        }
    }
}

@Composable
private fun LetterView(onOpenLetter: () -> Unit) {
    // Staggered line visibility
    val lineCount = 8
    val lineVisible = remember { List(lineCount) { Animatable(0f) } }

    LaunchedEffect(Unit) {
        for (i in lineVisible.indices) {
            delay(120L)
            lineVisible[i].animateTo(
                targetValue = 1f,
                animationSpec = tween(400, easing = EaseInOutCubic)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Letter paper
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(GlassCard)
                .padding(28.dp)
        ) {
            Column {
                // Line 0: greeting
                Text(
                    text = "致 ln：",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D2D2D),
                    fontFamily = FontFamily.Default,
                    modifier = Modifier.alpha(lineVisible[0].value)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Line 1
                Text(
                    text = "是你先打给我的。",
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.alpha(lineVisible[1].value)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Line 2
                Text(
                    text = "那时候只是同学间的闲聊，",
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.alpha(lineVisible[2].value)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Line 3
                Text(
                    text = "谁也没想到，这一聊就是两年。",
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.alpha(lineVisible[3].value)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Line 4
                Text(
                    text = "你的温柔都藏在细节里，",
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.alpha(lineVisible[4].value)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Line 5
                Text(
                    text = "让我在不知不觉中沦陷。",
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.alpha(lineVisible[5].value)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Line 6
                Text(
                    text = "两年了，有些话，",
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.alpha(lineVisible[6].value)
                )

                Text(
                    text = "想认真地告诉你——",
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.alpha(lineVisible[6].value)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Line 7: signature
                Text(
                    text = "—— wh",
                    fontSize = 16.sp,
                    color = Color(0xFF6A6A6A),
                    modifier = Modifier
                        .align(Alignment.End)
                        .alpha(lineVisible[7].value)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Continue button
        Button(
            onClick = onOpenLetter,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B81)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "翻开回忆  →",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun StarParticles() {
    val stars = remember {
        List(35) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 2.5f + 0.5f,
                alpha = Random.nextFloat() * 0.6f + 0.2f,
                speed = Random.nextFloat() * 0.3f + 0.1f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinkle"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        stars.forEach { star ->
            // Twinkle effect using sin wave
            val phase = (twinkle + star.speed * 360f) % 360f
            val twinkleAlpha = (sin(Math.toRadians(phase.toDouble())).toFloat() * 0.3f + 0.7f)

            drawCircle(
                color = Color.White.copy(alpha = star.alpha * twinkleAlpha),
                radius = star.size,
                center = Offset(star.x * w, star.y * h)
            )
        }
    }
}

private data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val alpha: Float,
    val speed: Float
)
