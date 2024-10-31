package com.example.xddemo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xddemo.ui.theme.MyApplicationTheme
import com.example.xddemo.ui.viewmodel.DownloadStatus

@Composable
fun DownloadCard(
    threadId: Int,
    status: DownloadStatus
) {
    Surface(
        elevation = 1.dp,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable { }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = ">>No.$threadId",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = when (status) {
                        DownloadStatus.DOWNLOADING -> "下载中..."
                        DownloadStatus.UPDATING -> "更新中..."
                        DownloadStatus.COMPLETE -> "已完成"
                    }
                )
            }
            if (status != DownloadStatus.COMPLETE) {
                Row(
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

        }
    }
}

@Preview
@Composable
fun DownloadCardPreview() {
    MyApplicationTheme {
        DownloadCard(threadId = 114514, status = DownloadStatus.UPDATING)
    }
}

@Preview
@Composable
fun DownloadCardPreview1() {
    MyApplicationTheme {
        DownloadCard(threadId = 114514, status = DownloadStatus.DOWNLOADING)
    }
}