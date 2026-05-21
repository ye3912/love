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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.night.memo.ui.theme.BlushPink
import com.night.memo.ui.theme.Gold
import com.night.memo.ui.theme.HotPink
import com.night.memo.ui.theme.RosePetal
import com.night.memo.ui.theme.SoftRose
import com.night.memo.ui.theme.WarmCream
import kotlinx.coroutines.delay
import kotlin.math.sin

data class TimelineCard(
    val title: String,
    val emoji: String,
    val lines: List<String>,
    val rotation: Float
)

@Composable
fun TimelineScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cards = remember {
        listOf(
            TimelineCard(
                title = "第一通电话",
                emoji = "\uD83D\uDCDE",
                lines = listOf(
                    "是你先打给我的。",
                    "那时候真的只是同学聊天，",
                    "谁也没想到这一聊就是两年。"
                ),
                rotation = -1.5f
            ),
            TimelineCard(
                title = "深夜电台",
                emoji = "\uD83C\uDF19",
                lines = listOf(
                    "不知道从什么时候开始，",
                    "每天最期待的就是你的电话。",
                    "你的关心总是藏在细节里，",
                    "温柔到让人一点一点沦陷。"
                ),
                rotation = 2f
            ),
            TimelineCard(
                title = "5月4日",
                emoji = "\uD83C\uDF38",
                lines = listOf(
                    "你先给了那个信号。",
                    "我才有勇气走向你。",
                    "这是我们故事真正的开始。"
                ),
                rotation = -1f
            )
        )
    }

    // Gold glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    // Timeline dot pulse
    val dotPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotPulse"
    )

    // Staggered card entrance
    val cardAnimations = remember {
        List(cards.size) { Animatable(0f) }
    }

    LaunchedEffect(Unit) {
        for (i in cardAnimations.indices) {
            delay(200L)
            cardAnimations[i].animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = EaseInOutCubic)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(WarmCream, BlushPink, RosePetal.copy(alpha = 0.3f))
                )
            )
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Title
            Text(
                text = "我们的故事",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3A5C),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "那些藏在时光里的温柔",
                fontSize = 14.sp,
                color = Color(0xFF8A7A9C),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(44.dp))

            // Timeline cards with staggered entrance
            cards.forEachIndexed { index, card ->
                val animProgress = cardAnimations[index].value
                val alpha = animProgress
                val offsetY = (1f - animProgress) * 40f

                Box(
                    modifier = Modifier
                        .alpha(alpha)
                        .padding(top = offsetY.dp)
                ) {
                    TimelineCardView(card = card)
                }

                // Timeline connector (except after last card)
                if (index < cards.lastIndex) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Pulsing connector line
                        for (i in 0 until 4) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .padding(vertical = 2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        SoftRose.copy(alpha = dotPulse * 0.6f)
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(44.dp))

            // Continue button with gold glow
            Box {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(width = 200.dp, height = 56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Gold.copy(alpha = glowAlpha * 0.3f)
                        )
                )

                Button(
                    onClick = onContinue,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.85f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    modifier = Modifier
                        .width(190.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "继续",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HotPink
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun TimelineCardView(
    card: TimelineCard,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .rotate(card.rotation)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        // Card header with emoji + title
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = card.emoji,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = card.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A2A4C)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card content lines
        card.lines.forEach { line ->
            Text(
                text = line,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                color = Color(0xFF5A4A6C),
                modifier = Modifier.padding(start = 30.dp)
            )
        }

        // Polaroid-style decorative bottom
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "\uD83D\uDCF7",
                fontSize = 14.sp,
                color = Color(0xFFCCCCCC)
            )
        }
    }
}
