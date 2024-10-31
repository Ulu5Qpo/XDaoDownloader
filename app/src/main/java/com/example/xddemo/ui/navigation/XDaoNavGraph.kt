package com.example.xddemo.ui.navigation

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.screens.SettingScreen
import com.example.xddemo.ui.screens.DownloadScreen
import com.example.xddemo.ui.screens.HomeScreen
import com.example.xddemo.ui.screens.ImageViewScreen
import com.example.xddemo.ui.screens.ReplyWithHeadScreen
import com.example.xddemo.ui.screens.SearchResultScreen
import com.example.xddemo.ui.screens.TestScreen
import com.example.xddemo.ui.viewmodel.DownloadViewModel

@Composable
fun XDaoNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val downloadViewModel: DownloadViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(500)
            )
        },
        modifier = modifier
    ) {
        composable(route = "home") {
            HomeScreen(
                navigateToReply = {
                    navController.navigate("replies/${it}")
                },
                navigateToOtherScreen = {
                    navController.navigate(it)
                },
                navigateToSearchResult = {
                    val encodedStr = Uri.encode(it)
                    navController.navigate("search/${encodedStr}")
                },
                navigateToImageView = {
                    val encodedUrl = Uri.encode(it)
                    navController.navigate("image/${encodedUrl}")
                },
                downloadViewModel = downloadViewModel
            )
        }

        composable(
            route = "replies/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            ReplyWithHeadScreen(
                threadId = id!!,
                navigateBack = { navController.navigateUp() },
                navigateToImageView = {
                    val encodedUrl = Uri.encode(it)
                    navController.navigate("image/${encodedUrl}")
                }
            )
        }

        composable(
            route = "replies/{threadId}/{replyId}",
            arguments = listOf(
                navArgument("threadId") { type = NavType.IntType },
                navArgument("replyId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val threadId = backStackEntry.arguments?.getInt("threadId")
            val replyId = backStackEntry.arguments?.getInt("replyId")

            ReplyWithHeadScreen(
                threadId = threadId!!,
                scrollToReplyId = replyId,
                navigateBack = { navController.navigateUp() },
                navigateToImageView = {
                    val encodedUrl = Uri.encode(it)
                    navController.navigate("image/${encodedUrl}")
                }
            )
        }

        composable(
            route = "search/{keywords}",
            arguments = listOf(navArgument("keywords") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val encodedStr = backStackEntry.arguments?.getString("keywords")
            val keywords = Uri.decode(encodedStr)
            SearchResultScreen(
                keywords = keywords,
                navigateBack = { navController.navigateUp() },
                navigateToReplyDetails = { threadId, replyId ->
                    navController.navigate("replies/$threadId/$replyId")
                }
            )
        }

        composable(
            route = "image/{url}",
            arguments = listOf(navArgument("url") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url")
            val url = Uri.decode(encodedUrl)
            ImageViewScreen(url = url!!)
        }

        composable(route = "download") {
            DownloadScreen(
                viewModel = downloadViewModel,
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(route = "setting") {
            SettingScreen()
        }
        composable(route = "test") {
            TestScreen()
        }
    }
}