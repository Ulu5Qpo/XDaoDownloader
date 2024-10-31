package com.example.xddemo.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Message
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.xddemo.R
import com.example.xddemo.data.mockThread
import com.example.xddemo.data.model.ThreadEntity
import com.example.xddemo.ui.theme.Karacha400
import com.example.xddemo.ui.theme.MyApplicationTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreadCard(
    threadEntity: ThreadEntity,
    onThreadClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onImageClick: (String) -> Unit,
    onUpdateClick: (Int) -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        elevation = 3.dp,
        modifier = modifier
            .padding(bottom = 16.dp)
            .combinedClickable(
                onClick = { onThreadClick(threadEntity.id) },
                onLongClick = { showDialog = true }
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = threadEntity.userHash,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = threadEntity.now,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .heightIn(max = 400.dp)
            ) {
                ThreadHtmlView(threadEntity.content)
            }
            if (threadEntity.img != "") {
                val imgUrl = "https://image.nmb.best/image/${threadEntity.img}${threadEntity.ext}"
                Box(
                    modifier = Modifier
                        .heightIn(max = 200.dp) // 缩略图的最大高度
                        .padding(horizontal = 12.dp)
                        .clickable { onImageClick(imgUrl) }
                ) {
                    AsyncImage(
                        model = imgUrl, // 根据实际情况拼接图片的 URL
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.ic_connection_error),
                        contentScale = ContentScale.Fit, // 保持长宽比
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.Bookmarks,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .size(18.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = threadEntity.replyCount.toString(),
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = ">>No.${threadEntity.id}") },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "更新",
                        fontSize = 20.sp,
                        color = Karacha400,
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 20.dp)
                            .clickable(onClick = {
                                onUpdateClick(threadEntity.id)
                                showDialog = false
                            })
                    )

                }
            },
            confirmButton = {}
        )
    }
}

@Preview
@Composable
fun ThreadCardPreview() {
    MyApplicationTheme {
        ThreadCard(threadEntity = mockThread, onThreadClick = {}, onImageClick = {})
    }
}