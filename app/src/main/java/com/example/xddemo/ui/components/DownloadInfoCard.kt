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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xddemo.R
import com.example.xddemo.ui.theme.MyApplicationTheme
import com.example.xddemo.ui.viewmodel.DownloadStatus

@Composable
fun DownloadCard(
    threadId: Int,
    status: DownloadStatus,
    errorMessage: String = ""
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
                        DownloadStatus.DOWNLOADING -> stringResource(R.string.download_status_downloading)
                        DownloadStatus.UPDATING -> stringResource(R.string.download_status_updating)
                        DownloadStatus.COMPLETE -> stringResource(R.string.download_status_complete)
                        DownloadStatus.ERROR -> stringResource(R.string.download_status_failed)
                    },
                    color = if (status == DownloadStatus.ERROR) Color.Red else Color.Unspecified
                )
            }
            if (status == DownloadStatus.ERROR && errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            if (status != DownloadStatus.COMPLETE && status != DownloadStatus.ERROR) {
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

@Preview
@Composable
fun DownloadCardPreviewError() {
    MyApplicationTheme {
        DownloadCard(
            threadId = 114514,
            status = DownloadStatus.ERROR,
            errorMessage = stringResource(
                R.string.error_download_failed,
                stringResource(R.string.error_network)
            )
        )
    }
}
