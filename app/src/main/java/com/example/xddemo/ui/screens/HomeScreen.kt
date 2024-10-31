package com.example.xddemo.ui.screens


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xddemo.R
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.components.DeleteButtonWithDialog
import com.example.xddemo.ui.components.SearchButtonWithDialog
import com.example.xddemo.ui.components.XDaoAppBar
import com.example.xddemo.ui.viewmodel.DownloadViewModel
import com.example.xddemo.ui.viewmodel.ThreadViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToReply: (Int) -> Unit,
    navigateToOtherScreen: (String) -> Unit,
    navigateToSearchResult: (String) -> Unit,
    navigateToImageView: (String) -> Unit,
    viewModel: ThreadViewModel = viewModel(factory = AppViewModelProvider.Factory),
    downloadViewModel: DownloadViewModel
) {

    val allThreads by viewModel.allThreads.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HomeScreenAppBar(
                title = "X岛离线版",
                onSearchConfirm = navigateToSearchResult,
                onNavIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerShape = customShape(),
        drawerContent = {
            AppDrawerContent(
                scaffoldState,
                scope,
                onScreenChange = navigateToOtherScreen
            )
        }
    ) {
        ThreadScreen(
            threads = allThreads.itemList,
            onThreadClick = navigateToReply,
            onImageClick = navigateToImageView,
            onUpdateClick = { downloadViewModel.startUpdate(it) }
        )
    }
}


@Composable
fun HomeScreenAppBar(
    title: String,
    onNavIconClick: () -> Unit,
    onSearchConfirm: (String) -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = onNavIconClick
            ) {
                Icon(Filled.Menu, null)
            }
        },
        actions = {
            SearchButtonWithDialog(
                navigateToResult = onSearchConfirm
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppDrawerContent(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    onScreenChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null
        )
        ListItem(
            icon = {
                Icon(Filled.RssFeed, null)
            },
            text = {
                Text("订阅")
            },
            modifier = Modifier
                .clickable { onScreenChange("test") }
        )
        ListItem(
            icon = {
                Icon(Filled.Download, null)
            },
            text = {
                Text("下载")
            },
            modifier = Modifier
                .clickable { onScreenChange("download") }
        )
        ListItem(
            icon = {
                Icon(Filled.Settings, null)
            },
            text = {
                Text("设置")
            },
            modifier = Modifier
                .clickable { onScreenChange("setting") }
        )
    }

    // 如果 drawer 已经展开了，那么点击返回键收起而不是直接退出 app
    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        scope.launch {
            delay(200)
            scaffoldState.drawerState.close()
        }
    }
}

fun customShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f, 0f, size.width * 0.8f, size.height))
    }
}