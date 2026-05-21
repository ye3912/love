package com.night.memo.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.night.memo.ui.theme.DeepRose
import com.night.memo.ui.theme.Gold
import com.night.memo.ui.theme.HotPink
import com.night.memo.ui.theme.RoyalPurple
import com.night.memo.ui.theme.VibrantPurple
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float,
    val type: Int // 0 = circle, 1 = heart
)

@Composable
fun FinaleScreen(
    modifier: Modifier = Modifier
) {
    var accepted by remember { mutableStateOf(false) }
    var showParticles by remember { mutableStateOf(false) }
    var showBurst by remember { mutableStateOf(false) }

    // Breathing animation for button
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    // Button glow
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonGlow"
    )

    // Text glow
    val textGlow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "textGlow"
    )

    // Typewriter effect for "我喜欢你"
    val fullText = "我喜欢你"
    val typewriterProgress = remember { Animatable(0f) }
    val displayedText = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in fullText.indices) {
            delay(250L)
            displayedText.value = fullText.substring(0, i + 1)
        }
    }

    // Start particles after brief delay
    LaunchedEffect(Unit) {
        delay(500)
        showParticles = true
    }

    // Heart burst effect on accept
    LaunchedEffect(accepted) {
        if (accepted) {
            showBurst = true
            delay(2000)
            showBurst = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        HotPink.copy(alpha = 0.8f),
                        VibrantPurple,
                        RoyalPurple.copy(alpha = 0.9f)
                    )
                )
            )
            .systemBarsPadding()
    ) {
        // Floating particles background
        if (showParticles) {
            FloatingParticles()
        }

        // Heart burst effect
        if (showBurst) {
            HeartBurst()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            // Main text: typewriter effect
            Text(
                text = displayedText.value,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Gold,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = Gold.copy(alpha = 0.6f * textGlow),
                        blurRadius = if (textGlow > 0.9f) 30f else 15f
                    )
                ),
                modifier = Modifier.scale(1f + (textGlow - 0.8f) * 0.1f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Subtitle with staggered fade-in
            val subtitleVisible = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                delay(1200L)
                subtitleVisible.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(600, easing = EaseInOutCubic)
                )
            }

            Text(
                text = "两年了，还是像高一那个夜晚一样心动。",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.85f * subtitleVisible.value),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Accept button
            Box(
                modifier = Modifier
                    .scale(if (accepted) 1f else breatheScale),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow
                if (!accepted) {
                    Box(
                        modifier = Modifier
                            .size(width = 260.dp, height = 64.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(
                                HotPink.copy(alpha = glowAlpha * 0.4f)
                            )
                    )
                }

                // Button
                Box(
                    modifier = Modifier
                        .size(width = 250.dp, height = 58.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(29.dp),
                            ambientColor = HotPink.copy(alpha = 0.3f),
                            spotColor = HotPink.copy(alpha = 0.5f)
                        )
                        .clip(RoundedCornerShape(29.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = if (accepted) listOf(Gold, Color(0xFFFFC107))
                                else listOf(HotPink, DeepRose)
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (!accepted) {
                                accepted = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (accepted) "\u2714 已收下" else "\u2665 收下这份心意",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer after acceptance
            if (accepted) {
                val footerVisible = remember { Animatable(0f) }
                LaunchedEffect(Unit) {
                    delay(300)
                    footerVisible.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(500, easing = EaseInOutCubic)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.alpha(footerVisible.value)
                ) {
                    Text(
                        text = "\u2764\uFE0F",
                        fontSize = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "谢谢你，让我遇见你。",
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "往后的日子，换我来守护你。",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun FloatingParticles() {
    val particles = remember {
        List(20) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 8f + 3f,
                speed = Random.nextFloat() * 0.002f + 0.001f,
                alpha = Random.nextFloat() * 0.5f + 0.2f,
                type = if (Random.nextFloat() > 0.5f) 1 else 0
            )
        }
    }

    val animatedValues = remember {
        particles.map {
            Animatable(Random.nextFloat() * 360f)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            animatedValues.forEach { animatable ->
                animatable.snapTo((animatable.value + 0.5f) % 360f)
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        particles.forEachIndexed { index, particle ->
            val angle = animatedValues[index].value
            val floatX = sin(Math.toRadians(angle.toDouble())).toFloat() * 20f
            val floatY = cos(Math.toRadians(angle.toDouble())).toFloat() * 10f - particle.speed * 500f

            val px = (particle.x * w + floatX).coerceIn(0f, w)
            val py = ((particle.y * h + floatY) % h).coerceIn(0f, h)

            if (particle.type == 0) {
                // Circle bubble
                drawCircle(
                    color = Color.White.copy(alpha = particle.alpha * 0.5f),
                    radius = particle.size,
                    center = Offset(px, py),
                    style = Stroke(width = 1.5f)
                )
            } else {
                // Heart shape
                val hs = particle.size * 0.8f
                drawCircle(
                    color = Color(0xFFFF6B81).copy(alpha = particle.alpha * 0.6f),
                    radius = hs,
                    center = Offset(px - hs * 0.5f, py)
                )
                drawCircle(
                    color = Color(0xFFFF6B81).copy(alpha = particle.alpha * 0.6f),
                    radius = hs,
                    center = Offset(px + hs * 0.5f, py)
                )
                val heartPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(px - hs * 1.2f, py + hs * 0.2f)
                    lineTo(px, py + hs * 1.6f)
                    lineTo(px + hs * 1.2f, py + hs * 0.2f)
                    close()
                }
                drawPath(
                    heartPath,
                    color = Color(0xFFFF6B81).copy(alpha = particle.alpha * 0.6f)
                )
            }
        }
    }
}

@Composable
private fun HeartBurst() {
    val burstParticles = remember {
        List(30) {
            BurstParticle(
                startX = 0.5f,
                startY = 0.55f,
                angle = Random.nextFloat() * 360f,
                speed = Random.nextFloat() * 8f + 4f,
                size = Random.nextFloat() * 12f + 6f,
                alpha = Random.nextFloat() * 0.5f + 0.5f
            )
        }
    }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(1500, easing = EaseInOutCubic)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        burstParticles.forEach { particle ->
            val distance = particle.speed * progress.value * 80f
            val px = (particle.startX * w + cos(Math.toRadians(particle.angle.toDouble())).toFloat() * distance)
            val py = (particle.startY * h + sin(Math.toRadians(particle.angle.toDouble())).toFloat() * distance - progress.value * 50f)

            val fadeAlpha = particle.alpha * (1f - progress.value * 0.7f)
            val scale = 1f + progress.value * 0.3f

            // Heart shape
            val hs = particle.size * scale
            drawCircle(
                color = Color(0xFFFF6B81).copy(alpha = fadeAlpha),
                radius = hs,
                center = Offset(px - hs * 0.5f, py)
            )
            drawCircle(
                color = Color(0xFFFF6B81).copy(alpha = fadeAlpha),
                radius = hs,
                center = Offset(px + hs * 0.5f, py)
            )
            val heartPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(px - hs * 1.2f, py + hs * 0.2f)
                lineTo(px, py + hs * 1.6f)
                lineTo(px + hs * 1.2f, py + hs * 0.2f)
                close()
            }
            drawPath(
                heartPath,
                color = Color(0xFFFF6B81).copy(alpha = fadeAlpha)
            )
        }
    }
}

private data class BurstParticle(
    val startX: Float,
    val startY: Float,
    val angle: Float,
    val speed: Float,
    val size: Float,
    val alpha: Float
)
