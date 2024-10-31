package com.example.xddemo.ui.components

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import android.graphics.Color

@Composable
fun ThreadHtmlView(htmlText: String) {

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                // 将 HTML 文本转换为 Spanned 对象
                text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        },
        update = { textView ->
            // 更新 HTML 文本
            textView.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textView.setTextSize(16f)
            textView.setTextColor(Color.parseColor("#212121"))
            textView.letterSpacing = 0.05f // 字符间距
            textView.setLineSpacing(8f, 1f) // 行间距
        }
    )
}