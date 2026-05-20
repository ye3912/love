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

sealed class Route(val route: String) {
    data object Intro : Route("intro")
    data object MemoList : Route("memo_list")
    data object MemoDetail : Route("memo_detail/{memoId}") {
        fun createRoute(memoId: Long) = "memo_detail/$memoId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Memo state lifted to navigation level for persistence across screens
    val memos = remember {
        mutableStateListOf(
            Memo(
                id = 1L,
                title = "05.04 Record",
                content = "", // TODO: Replace with custom content
                createdAt = 1714780800000L, // 2024-05-04 00:00:00 UTC
                isFavorite = false
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = Route.Intro.route
    ) {
        composable(Route.Intro.route) {
            IntroScreen(
                onGetStarted = {
                    navController.navigate(Route.MemoList.route) {
                        popUpTo(Route.Intro.route) { inclusive = true }
                    }
                },
                onSpecialAction = {
                    navController.navigate(Route.MemoList.route) {
                        popUpTo(Route.Intro.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.MemoList.route) {
            MemoListScreen(
                memos = memos.toList(),
                onMemoClick = { memoId ->
                    navController.navigate(Route.MemoDetail.createRoute(memoId))
                }
            )
        }

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
    }
}
