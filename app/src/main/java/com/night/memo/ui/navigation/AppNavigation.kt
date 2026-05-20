package com.night.memo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.night.memo.data.model.Memo
import com.night.memo.ui.screens.IntroScreen
import com.night.memo.ui.screens.MemoDetailScreen
import com.night.memo.ui.screens.MemoListScreen
import com.night.memo.ui.screens.TimelineScreen
import com.night.memo.ui.screens.FinaleScreen

sealed class Route(val route: String) {
    // Normal flow (facade)
    data object Intro : Route("intro")
    data object MemoList : Route("memo_list")
    data object MemoDetail : Route("memo_detail/{memoId}") {
        fun createRoute(memoId: Long) = "memo_detail/$memoId"
    }

    // Hidden romantic flow
    data object Timeline : Route("timeline")
    data object Finale : Route("finale")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Memo state for normal (facade) flow
    val memos = remember {
        mutableStateListOf(
            Memo(
                id = 1L,
                title = "05.04 Record",
                content = "",
                createdAt = 1714780800000L,
                isFavorite = false
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = Route.Intro.route
    ) {
        // ── INTRO (dual entry) ──
        composable(Route.Intro.route) {
            IntroScreen(
                onGetStarted = {
                    // Normal click → Memo facade
                    navController.navigate(Route.MemoList.route) {
                        popUpTo(Route.Intro.route) { inclusive = true }
                    }
                },
                onOpenLetter = {
                    // Long press → open letter → navigate to Timeline (hidden)
                    navController.navigate(Route.Timeline.route) {
                        // Don't pop intro so user can go back
                    }
                }
            )
        }

        // ── NORMAL FLOW: Memo List (facade) ──
        composable(Route.MemoList.route) {
            MemoListScreen(
                memos = memos.toList(),
                onMemoClick = { memoId ->
                    navController.navigate(Route.MemoDetail.createRoute(memoId))
                }
            )
        }

        // ── NORMAL FLOW: Memo Detail (facade) ──
        composable(Route.MemoDetail.route) { backStackEntry ->
            val memoId = backStackEntry.arguments?.getString("memoId")?.toLongOrNull() ?: 1L
            val memo = memos.find { it.id == memoId } ?: return@composable

            MemoDetailScreen(
                memo = memo,
                onBack = { navController.popBackStack() },
                onFavoriteToggle = { target ->
                    val index = memos.indexOfFirst { it.id == target.id }
                    if (index >= 0) {
                        memos[index] = target.copy(isFavorite = !target.isFavorite)
                    }
                }
            )
        }

        // ── HIDDEN FLOW: Timeline (memories) ──
        composable(Route.Timeline.route) {
            TimelineScreen(
                onContinue = {
                    navController.navigate(Route.Finale.route)
                }
            )
        }

        // ── HIDDEN FLOW: Finale (confession) ──
        composable(Route.Finale.route) {
            FinaleScreen()
        }
    }
}
