package com.example.xddemo.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.toReplyEntity
import com.example.xddemo.data.repository.ReplyPositionPreferences
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.components.DeleteButtonWithDialog
import com.example.xddemo.ui.components.PageSliderDialogButton
import com.example.xddemo.ui.components.ReplyCard
import com.example.xddemo.ui.theme.MyApplicationTheme
import com.example.xddemo.ui.viewmodel.ThreadViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.ceil

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReplyWithHeadScreen(
    context: Context = LocalContext.current,
    threadId: Int,
    scrollToReplyId: Int? = null,
    navigateBack: () -> Unit,
    navigateToImageView: (String) -> Unit,
    viewModel: ThreadViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()
    val threadWithReplies by viewModel.threadWithReplies.collectAsState()

    val isInitialEntry = rememberSaveable { mutableStateOf(true) }
    val listState = rememberLazyListState()
    var curPage by remember { mutableIntStateOf(1) }
    var totalPage by remember { mutableIntStateOf(1) }

    val isOnlyPoster = remember { mutableStateOf(false) }
    val quoteStack = remember { mutableStateListOf<ReplyEntity?>() } // 栈来存储对话框内容

    LaunchedEffect(threadId) {
        viewModel.getThreadWithReplies(threadId)
    }

    val filteredReplies = threadWithReplies?.replies?.filter {
        !isOnlyPoster.value || it.userHash == threadWithReplies?.thread?.userHash
    } ?: emptyList()

    LaunchedEffect(filteredReplies) {
        totalPage = ceil(filteredReplies.size / 19.0).toInt()
        Log.d("selectPageDetail", "totalPage: $totalPage")
    }

    LaunchedEffect(threadWithReplies) {
        if (isInitialEntry.value && threadWithReplies != null && scrollToReplyId != null) {
            val index = threadWithReplies!!.replies.indexOfFirst { it.id == scrollToReplyId } + 1
            Log.d("scrollReplyDetail", "scrollToReplyId: $scrollToReplyId, index: $index")
            if (index >= 0) {
                listState.scrollToItem(index)
                isInitialEntry.value = false
            }
        }
    }

    // 从 DataStore 中获取保存的位置
    val savedPosition by remember {
        ReplyPositionPreferences.getPosition(context, threadId)
            .stateIn(scope, SharingStarted.WhileSubscribed(5000), initialValue = 0)
    }.collectAsState()

    // 恢复浏览位置
    if (scrollToReplyId == null && isInitialEntry.value) {
        LaunchedEffect(savedPosition) {
            snapshotFlow { listState.layoutInfo.totalItemsCount }
                .filter { it > 0 } // 确保 LazyColumn 已经有内容
                .firstOrNull {
                    // 当 LazyColumn 加载完毕时，执行滚动
                    savedPosition?.let {
                        listState.scrollToItem(it)
                        Log.d("recoverSavedPosition", "recoverSavedPosition: $savedPosition")
                    }
                    isInitialEntry.value = false
                    true // 立即返回，终止流监听
                }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex / 19 + 1 }
            .distinctUntilChanged() // 仅在页面数发生变化时触发
            .collect { newPage ->
                if (curPage != newPage) {
                    curPage = newPage
                }
                Log.d("selectPageDetail", "curPage: $curPage")
            }
    }

    Scaffold(
        topBar = {
            ReplyAppBar(
                title = "No.$threadId",
                onConfirmAction = { viewModel.deleteThread() },
                onNavIconClick = navigateBack,
                curPage = curPage,
                totalPage = totalPage,
                onPageSelect = {
                    scope.launch { listState.scrollToItem((curPage - 1) * 19 + 1) }
                    curPage = it
                },
                onOnlyPosterClick = {
                    isOnlyPoster.value = !isOnlyPoster.value
                }
            )
        }
    ) {
        threadWithReplies?.let { threadWithReplies ->
            LazyColumn(state = listState) {
                item {
                    ReplyCard(
                        poster = threadWithReplies.thread.userHash,
                        replyEntity = threadWithReplies.thread.toReplyEntity(),
                        onImageClick = navigateToImageView,
                        onQuoteClick = { quoteId ->
                            scope.launch {
                                val quoteReply = viewModel.getSingleReply(quoteId.toInt())
                                quoteStack.add(quoteReply) // 将引用的Reply加入栈
                            }
                        }
                    )
                    Divider()
                }
                items(filteredReplies) { reply ->
                    ReplyCard(
                        poster = threadWithReplies.thread.userHash,
                        replyEntity = reply,
                        onImageClick = navigateToImageView,
                        onQuoteClick = { quoteId ->
                            scope.launch {
                                val quoteReply = viewModel.getSingleReply(quoteId.toInt())
                                quoteStack.add(quoteReply) // 将引用的Reply加入栈
                            }
                        }
                    )
                    Divider()
                }
            }
        }

        // 当 quoteStack 不为空时显示对话框
        if (quoteStack.isNotEmpty()) {
            Dialog(
                onDismissRequest = { quoteStack.removeLast() } // 关闭对话框时弹出栈顶
            ) {
                Card(
                    modifier = Modifier
                        .heightIn(min = 100.dp)
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(2.dp),
                ) {
                    val replyEntity = quoteStack.last() // 显示栈顶引用的Reply内容
                    if (replyEntity == null) {
                        Text(
                            text = "数据库中无对应数据",
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)
                        )
                    } else {
                        ReplyCard(
                            poster = threadWithReplies!!.thread.userHash,
                            replyEntity = replyEntity,
                            onImageClick = navigateToImageView,
                            onQuoteClick = { quoteId ->
                                scope.launch {
                                    val nestedQuoteReply = viewModel.getSingleReply(quoteId.toInt())
                                    quoteStack.add(nestedQuoteReply)  // 将新的引用加入栈
                                }
                            }
                        )
                    }

                }
            }
        }

    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d("ReplyWithHeadScreen", "Lifecycle Event: $event")
            if (event == Lifecycle.Event.ON_STOP) {
                scope.launch {
                    ReplyPositionPreferences.savePosition(
                        context,
                        threadId,
                        listState.firstVisibleItemIndex
                    )
                    Log.d(
                        "SavingPosition",
                        "threadId: $threadId  position: ${listState.firstVisibleItemIndex}"
                    )
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            Log.d("ReplyWithHeadScreen", "onDispose called")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

}

@Composable
fun ReplyAppBar(
    title: String,
    curPage: Int,
    totalPage: Int,
    onNavIconClick: () -> Unit,
    onConfirmAction: () -> Unit,
    onPageSelect: (Int) -> Unit,
    onOnlyPosterClick: () -> Unit
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
            IconButton(
                onClick = onOnlyPosterClick
            ) {
                Icon(Filled.CenterFocusStrong, null)
            }
            key(curPage) {
                PageSliderDialogButton(
                    initialPage = curPage,
                    totalPage = totalPage,
                    onPageSelected = onPageSelect
                )
            }
            DeleteButtonWithDialog(
                onConfirmAction = onConfirmAction,
                navigateUp = onNavIconClick
            )

        }
    )
}


@Preview
@Composable
fun ReplyWithHeadScreenPreview() {
    MyApplicationTheme {

    }
}