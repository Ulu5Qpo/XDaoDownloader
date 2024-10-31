package com.example.xddemo.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.xddemo.ui.theme.MyApplicationTheme

@Composable
fun XDaoAppBar(
    title: String,
    canNavigateBack: Boolean,
    onNavIconClick: () -> Unit,
    onConfirmAction: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = onNavIconClick
            ) {
                if (canNavigateBack) {
                    Icon(Filled.ArrowBack, null)
                } else {
                    Icon(Filled.Menu, null)
                }
            }
        },
        actions = {
            if (canNavigateBack) {
                DeleteButtonWithDialog(
                    onConfirmAction = onConfirmAction,
                    navigateUp = onNavIconClick
                )
            } else {
                SearchButtonWithDialog (
                    navigateToResult = {}
                )
            }
        }
    )
}

@Preview
@Composable
fun AppBarPreview() {
    MyApplicationTheme {
        XDaoAppBar(
            title = "主页",
            canNavigateBack = false,
            onNavIconClick = {},
            onConfirmAction = {}
        )
    }
}

@Preview
@Composable
fun AppBarPreview2() {
    MyApplicationTheme {
        XDaoAppBar(
            title = "下载",
            canNavigateBack = true,
            onNavIconClick = {},
            onConfirmAction = {}
        )
    }
}