package com.example.xddemo.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.xddemo.data.mockThread
import com.example.xddemo.data.mockThreads
import com.example.xddemo.data.model.ThreadEntity
import com.example.xddemo.ui.components.ThreadCard
import com.example.xddemo.ui.theme.MyApplicationTheme

@Composable
fun ThreadScreen(
    threads: List<ThreadEntity>,
    onThreadClick: (Int) -> Unit,
    onImageClick: (String) -> Unit = {},
    onUpdateClick: (Int) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        LazyColumn {
            items(threads) { threadEntity ->
                ThreadCard(
                    threadEntity = threadEntity,
                    onThreadClick = onThreadClick,
                    onImageClick = onImageClick,
                    onUpdateClick = onUpdateClick
                )
            }
        }
    }

}

@Preview
@Composable
fun ThreadScreenPreview() {
    MyApplicationTheme {
        ThreadScreen(threads = mockThreads, onThreadClick = {}, {})
    }
}
