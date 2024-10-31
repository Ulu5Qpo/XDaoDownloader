package com.example.xddemo.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.components.DownloadButtonWithDialog
import com.example.xddemo.ui.components.DownloadCard
import com.example.xddemo.ui.components.SearchButtonWithDialog
import com.example.xddemo.ui.viewmodel.DownloadViewModel
import com.example.xddemo.ui.viewmodel.ThreadViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DownloadScreen(
    navigateBack: () -> Unit,
    viewModel: DownloadViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val downloadList by viewModel.downloadList.collectAsState()

    Scaffold(
        topBar = {
            DownloadAppBar(
                title = "下载列表",
                onNavIconClick = navigateBack,
                onDownloadConfirm = {
                    viewModel.startDownload(it)
                }
            )
        }
    ) {
        Surface(
            color = Color(0xFFFAFAFA),
            modifier = Modifier.fillMaxSize()
        ) {
            if (downloadList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "请避免同时添加多个下载任务",
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
            }
            LazyColumn {
                items(downloadList) { downloadState ->
                    DownloadCard(
                        threadId = downloadState.threadId,
                        status = downloadState.status
                    )
                }
            }
        }
    }
}

@Composable
fun DownloadAppBar(
    title: String,
    onNavIconClick: () -> Unit,
    onDownloadConfirm: (Int) -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = onNavIconClick
            ) {
                Icon(Filled.ArrowBack, null)
            }
        },
        actions = {
            DownloadButtonWithDialog(onDownloadConfirm)
        }
    )
}