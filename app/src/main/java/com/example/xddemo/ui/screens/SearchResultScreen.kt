package com.example.xddemo.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.components.DeleteButtonWithDialog
import com.example.xddemo.ui.components.ReplyCard
import com.example.xddemo.ui.components.SearchButtonWithDialog
import com.example.xddemo.ui.components.XDaoAppBar
import com.example.xddemo.ui.viewmodel.ThreadViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchResultScreen(
    keywords: String,
    navigateBack: () -> Unit,
    navigateToReplyDetails: (Int, Int) -> Unit,
    viewModel: ThreadViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val searchResult by viewModel.searchResults.collectAsState()

    LaunchedEffect(keywords) {
        viewModel.searchByKeywords(keywords)
    }

    Scaffold(
        topBar = {
            SearchScreenAppBar(
                title = "搜索结果",
                onNavIconClick = navigateBack,
            )
        }
    ) {
        Surface(
            color = Color(0xFFFAFAFA),
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn {
                items(searchResult) { reply ->
                    ReplyCard(
                        poster = "",
                        modifier = Modifier.padding(bottom = 16.dp),
                        hasElevation = true,
                        replyEntity = reply,
                        onReplyClick = navigateToReplyDetails,
                        onImageClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun SearchScreenAppBar(
    title: String,
    onNavIconClick: () -> Unit,
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
        }
    )
}