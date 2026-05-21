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
                    "那天你打来的时候，",
                    "窗外的风刚好停了。",
                    "我们聊了些无关紧要的事——",
                    "作业、老师、周末的计划。",
                    "你的声音穿过信号落在我耳边，",
                    "像月光落在水面上，",
                    "轻轻的，却亮了整个夜晚。",
                    "挂了电话才发现，",
                    "原来风一直在听。",
                    "那时候我还不知道，",
                    "这通电话会成为一个故事的开头。",
                    "有些相遇，就是这样——",
                    "没有预兆，没有准备，",
                    "只是刚好你打来，",
                    "刚好我在。",
                    "后来我才明白，",
                    "世间所有的久别重逢，",
                    "都是久别后的久别。",
                    "而我们的故事，",
                    "从那一通电话开始，",
                    "就没有再挂断过。"
                ),
                rotation = -1.5f
            ),
            TimelineCard(
                title = "深夜电台",
                emoji = "\uD83C\uDF19",
                lines = listOf(
                    "后来的深夜，",
                    "电话两头隔着整座城市的灯火。",
                    "我们聊今天发生了什么，",
                    "聊那些说不出口的心事，",
                    "聊到手机发烫也不舍得挂断。",
                    "有时候什么都不说，",
                    "就那样安静地待着，",
                    "听彼此的呼吸声。",
                    "你说的那些梦想，",
                    "我都偷偷记在心里了。",
                    "每次你说"晚安"，",
                    "我都觉得这一天没有白过。",
                    "不知道从什么时候开始，",
                    "入睡成了一件困难的事——",
                    "因为习惯了你的声音才能安心。",
                    "深夜的电话线，",
                    "像一根看不见的绳，",
                    "把两颗心轻轻系在一起。",
                    "你说的每一句话，",
                    "我都放在心里最柔软的地方。",
                    "那些深夜的对话，",
                    "像星星一样，",
                    "点亮了我整个青春。"
                ),
                rotation = 2f
            ),
            TimelineCard(
                title = "5月4日",
                emoji = "\uD83C\uDF38",
                lines = listOf(
                    "那天你在QQ上突然问我，",
                    "猜猜你给我的备注是什么意思。",
                    "我的心跳漏了一拍。",
                    "其实我心里一直有答案，",
                    "但就是不敢开口。",
                    "是你先迈出的那一步，",
                    "我才终于有了勇气。",
                    "像春风递过来的第一封信，",
                    "我接住了，",
                    "从此故事有了名字。",
                    "五月四日，",
                    "我会一直记得这个日子。",
                    "有些人，一旦遇见，",
                    "便是一生。",
                    "有些事，一旦开始，",
                    "便再也回不去。",
                    "而我们的故事，",
                    "从你迈出那一步开始，",
                    "就再也停不下来了。",
                    "谢谢你，",
                    "让我的青春有了颜色。",
                    "谢谢你，",
                    "让我知道什么是喜欢。"
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
                lineHeight = 26.sp,
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
