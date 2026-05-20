package com.night.memo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.night.memo.ui.theme.MidnightBlue
import com.night.memo.ui.theme.RoyalPurple
import com.night.memo.ui.theme.SoftPurple
import com.night.memo.ui.theme.GlassWhite
import com.night.memo.ui.theme.GlassBorder
import com.night.memo.ui.theme.GlassCard
import com.night.memo.ui.theme.CrimsonRed
import com.night.memo.ui.theme.DeepIndigo
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreen(
    onGetStarted: () -> Unit,
    onOpenLetter: () -> Unit,
    modifier: Modifier = Modifier
) {
    var letterOpened by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        showContent = true
    }

    // Beating heart animation
    val infiniteTransition = rememberInfiniteTransition(label = "heart")
    val heartScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = androidx.compose.animation.core.EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartBeat"
    )

    // Envelope float animation
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = androidx.compose.animation.core.EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
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
                    if (letterOpened) {
                        // Letter is showing — do nothing on tap
                    } else {
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
        // Aurora light spots (decorative)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top-right glow
            drawCircle(
                color = SoftPurple.copy(alpha = 0.15f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.8f, size.height * 0.2f)
            )
            // Bottom-left glow
            drawCircle(
                color = Color(0xFF6A4ABE).copy(alpha = 0.1f),
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

                // Envelope
                Box(
                    modifier = Modifier
                        .size(220.dp, 160.dp)
                        .scale(1f + floatOffset / 400f)
                ) {
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

                        // Envelope flap (top triangle)
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

                        // Flap border line
                        val flapLine = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(w / 2, h * 0.38f)
                            lineTo(w, 0f)
                        }
                        drawPath(flapLine, color = Color(0xFFE0E0E0), style = Stroke(1.dp.toPx()))

                        // Red ribbon (horizontal seam)
                        drawLine(
                            color = CrimsonRed,
                            start = Offset(0f, h / 2),
                            end = Offset(w, h / 2),
                            strokeWidth = 3.dp.toPx()
                        )

                        // Ribbon bow (center knot)
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

                Spacer(modifier = Modifier.height(32.dp))

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

                // Subtle hint at bottom
                Text(
                    text = "轻触进入备忘录",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        } else {
            // === LETTER VIEW ===
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(800)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(600)) +
                        slideInVertically(
                            initialOffsetY = { it / 4 },
                            animationSpec = tween(600)
                        )
            ) {
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
                            Text(
                                text = "致 ln：",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2D2D2D),
                                fontFamily = FontFamily.Default
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "是你先打给我的。\n" +
                                        "那时候只是同学聊天，\n" +
                                        "没想到会聊过一天又一天。",
                                fontSize = 16.sp,
                                lineHeight = 28.sp,
                                color = Color(0xFF4A4A4A),
                                fontFamily = FontFamily.Default
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "你的温柔藏在细节里，\n" +
                                        "让我一点一点沦陷。",
                                fontSize = 16.sp,
                                lineHeight = 28.sp,
                                color = Color(0xFF4A4A4A)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "两年了，有些话\n" +
                                        "想好好告诉你——",
                                fontSize = 16.sp,
                                lineHeight = 28.sp,
                                color = Color(0xFF4A4A4A)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "—— wh",
                                fontSize = 16.sp,
                                color = Color(0xFF6A6A6A),
                                modifier = Modifier.align(Alignment.End)
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
        }
    }
}
