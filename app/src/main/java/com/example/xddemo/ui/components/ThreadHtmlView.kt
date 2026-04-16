package com.example.xddemo.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import android.widget.TextView

@Composable
fun ThreadHtmlView(htmlText: String) {
    val textColor = MaterialTheme.colors.onSurface.toArgb()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
                setTextSize(16f)
                setTextColor(textColor)
                letterSpacing = 0.05f
                setLineSpacing(8f, 1f)
            }
        },
        update = { textView ->
            textView.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textView.setTextColor(textColor)
        }
    )
}
