package com.example.xddemo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.xddemo.R
import com.example.xddemo.data.mockReply
import com.example.xddemo.data.mockThread
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity
import com.example.xddemo.ui.theme.MyApplicationTheme


@Composable
fun ReplyCard(
    modifier: Modifier = Modifier,
    poster: String,
    replyEntity: ReplyEntity,
    hasElevation: Boolean = false,
    onImageClick: (String) -> Unit,
    onReplyClick: (Int, Int) -> Unit = { _, _ -> },
    onQuoteClick: (String) -> Unit = {}
) {
    Surface(
        elevation = if (hasElevation) 3.dp else 0.dp,
        modifier = modifier.clickable { onReplyClick(replyEntity.threadId, replyEntity.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = replyEntity.userHash,
                    color = if (replyEntity.userHash == poster) Color(0xFF789922) else Color.Gray,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "No." + replyEntity.id.toString(),
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                if (hasElevation) {
                    ThreadHtmlView(replyEntity.content)
                } else {
                    HtmlTextView(replyEntity.content, onQuoteClick = onQuoteClick)
                }

            }
            if (replyEntity.img != "") {
                val imgUrl = "https://image.nmb.best/image/${replyEntity.img}${replyEntity.ext}"
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
                Text(
                    text = replyEntity.now,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ReplyCardPreview() {
    MyApplicationTheme {
        ReplyCard(replyEntity = mockReply, onImageClick = {}, poster = "")
    }
}