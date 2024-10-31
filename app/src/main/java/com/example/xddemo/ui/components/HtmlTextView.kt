package com.example.xddemo.ui.components

import android.widget.TextView
import androidx.compose.runtime.Composable
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlTextView(
    htmlText: String,
    onQuoteClick: (String) -> Unit
) {
    // 状态管理，控制哪些遮挡条块已被点击
    val hiddenContentState = remember { mutableStateOf(setOf<Int>()) }

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = parseHtmlWithCustomSpans(htmlText, hiddenContentState, onQuoteClick)
                setTextSize(16f)
                setTextColor(Color.parseColor("#212121"))
                letterSpacing = 0.05f
                setLineSpacing(8f, 1f)
                movementMethod = LinkMovementMethod.getInstance() // 确保点击事件生效
            }
        },
        update = { textView ->
            textView.text = parseHtmlWithCustomSpans(htmlText, hiddenContentState, onQuoteClick)
        },
    )
}

// 解析 HTML 并应用自定义可点击的 span
fun parseHtmlWithCustomSpans(
    htmlText: String,
    hiddenContentState: MutableState<Set<Int>>,
    onQuoteClick: (String) -> Unit
): Spannable {
    // 初步解析 HTML
    val spannableBuilder = SpannableStringBuilder(
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    )

    // 正则匹配 ">>No.数字" 的引用
    val quotePattern = Regex(">>No\\.(\\d+)")
    val quoteMatches = quotePattern.findAll(spannableBuilder)

    quoteMatches.forEach { matchResult ->
        val start = matchResult.range.first
        val end = matchResult.range.last + 1
        val quoteId = matchResult.groupValues[1]

        spannableBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                onQuoteClick(quoteId)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.rgb(120,153,34)

            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    // 正则匹配 "[h] 内容 [/h]" 模式
    val hiddenPattern = Regex("\\[h](.*?)\\[/h]")
    val hiddenMatches = hiddenPattern.findAll(spannableBuilder)

    hiddenMatches.forEach { matchResult ->
        val start = matchResult.range.first
        val end = matchResult.range.last + 1
        val content = matchResult.groupValues[1] // 提取遮挡内容
        val hashCode = matchResult.range.hashCode() // 用匹配的范围创建唯一标识
        val isContentVisible = hiddenContentState.value.contains(hashCode)

        spannableBuilder.replace(start, end, content)
        if (isContentVisible) {
            // 显示实际内容
            //spannableBuilder.replace(start, end, content)
        } else {
            //spannableBuilder.replace(start, end, content)
            val contentStart = start
            val contentEnd = contentStart + content.length

            spannableBuilder.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    hiddenContentState.value = hiddenContentState.value + hashCode
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.BLACK // 黑色遮挡效果
                    ds.bgColor = Color.BLACK // 深色背景
                    ds.isUnderlineText = false
                }
            }, contentStart, contentEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    return spannableBuilder
}