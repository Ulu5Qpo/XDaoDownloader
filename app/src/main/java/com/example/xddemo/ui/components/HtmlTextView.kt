package com.example.xddemo.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.example.xddemo.R

@Composable
fun HtmlTextView(
    htmlText: String,
    onQuoteClick: (String) -> Unit
) {
    val hiddenContentState = remember(htmlText) { mutableStateOf(emptySet<Int>()) }
    val textColor = MaterialTheme.colors.onSurface.toArgb()
    val quoteColor = android.graphics.Color.rgb(120, 153, 34)
    val hiddenColor = MaterialTheme.colors.onSurface.toArgb()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                setTextSize(16f)
                letterSpacing = 0.05f
                setLineSpacing(8f, 1f)
                highlightColor = Color.TRANSPARENT
                setTextColor(textColor)
                setText(
                    parseHtmlWithCustomSpans(
                        htmlText = htmlText,
                        hiddenContentState = hiddenContentState,
                        onQuoteClick = onQuoteClick,
                        quoteColor = quoteColor,
                        hiddenForegroundColor = hiddenColor,
                        hiddenBackgroundColor = hiddenColor
                    ),
                    TextView.BufferType.SPANNABLE
                )
                val longPressCopy = {
                    copyTextToClipboard(context, text)
                    true
                }
                setOnLongClickListener {
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    longPressCopy()
                }
                setOnTouchListener(HtmlTextTouchListener(onLongPress = {
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    longPressCopy()
                }))
            }
        },
        update = { textView ->
            textView.setTextColor(textColor)
            textView.setText(
                parseHtmlWithCustomSpans(
                    htmlText = htmlText,
                    hiddenContentState = hiddenContentState,
                    onQuoteClick = onQuoteClick,
                    quoteColor = quoteColor,
                    hiddenForegroundColor = hiddenColor,
                    hiddenBackgroundColor = hiddenColor
                ),
                TextView.BufferType.SPANNABLE
            )
        },
    )
}

fun parseHtmlWithCustomSpans(
    htmlText: String,
    hiddenContentState: MutableState<Set<Int>>,
    onQuoteClick: (String) -> Unit,
    quoteColor: Int,
    hiddenForegroundColor: Int,
    hiddenBackgroundColor: Int
): Spannable {
    val spannableBuilder = SpannableStringBuilder(
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    )

    val hiddenPattern = Regex("\\[h](.*?)\\[/h]")
    val hiddenMatches = hiddenPattern.findAll(spannableBuilder.toString()).toList().asReversed()

    hiddenMatches.forEach { matchResult ->
        val start = matchResult.range.first
        val end = matchResult.range.last + 1
        val content = matchResult.groupValues[1]
        val hashCode = matchResult.range.hashCode()
        val isContentVisible = hiddenContentState.value.contains(hashCode)

        spannableBuilder.replace(start, end, content)
        if (!isContentVisible) {
            val contentStart = start
            val contentEnd = contentStart + content.length

            spannableBuilder.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    hiddenContentState.value = hiddenContentState.value + hashCode
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = hiddenForegroundColor
                    ds.bgColor = hiddenBackgroundColor
                    ds.isUnderlineText = false
                }
            }, contentStart, contentEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    val quotePattern = Regex(">>No\\.(\\d+)")
    val quoteMatches = quotePattern.findAll(spannableBuilder.toString()).toList()

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
                ds.color = quoteColor
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return spannableBuilder
}

private class HtmlTextTouchListener(
    private val onLongPress: () -> Unit
) : View.OnTouchListener {
    private var pressedSpan: ClickableSpan? = null
    private var longPressTriggered = false
    private var downX = 0f
    private var downY = 0f
    private var activeTextView: TextView? = null
    private val longPressRunnable = Runnable {
        if (pressedSpan != null) {
            longPressTriggered = true
            onLongPress()
        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val textView = view as? TextView ?: return false
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> handleActionDown(textView, event)
            MotionEvent.ACTION_MOVE -> handleActionMove(textView, event)
            MotionEvent.ACTION_UP -> handleActionUp(textView, event)
            MotionEvent.ACTION_CANCEL -> {
                clearPressedState(textView)
                false
            }

            else -> false
        }
    }

    private fun handleActionDown(textView: TextView, event: MotionEvent): Boolean {
        val span = findClickableSpan(textView, event) ?: return false
        pressedSpan = span
        activeTextView = textView
        longPressTriggered = false
        downX = event.x
        downY = event.y
        textView.postDelayed(
            longPressRunnable,
            ViewConfiguration.getLongPressTimeout().toLong()
        )
        return true
    }

    private fun handleActionMove(textView: TextView, event: MotionEvent): Boolean {
        val currentSpan = pressedSpan ?: return false
        val touchSlop = ViewConfiguration.get(textView.context).scaledTouchSlop
        val movedTooFar =
            kotlin.math.abs(event.x - downX) > touchSlop || kotlin.math.abs(event.y - downY) > touchSlop

        if (movedTooFar || findClickableSpan(textView, event) != currentSpan) {
            clearPressedState(textView)
            return false
        }
        return true
    }

    private fun handleActionUp(textView: TextView, event: MotionEvent): Boolean {
        val span = pressedSpan ?: return false
        val handledByLongPress = longPressTriggered
        val shouldTriggerClick = !handledByLongPress && findClickableSpan(textView, event) == span

        clearPressedState(textView)

        if (shouldTriggerClick) {
            span.onClick(textView)
        }
        return true
    }

    private fun clearPressedState(textView: TextView) {
        textView.removeCallbacks(longPressRunnable)
        activeTextView?.removeCallbacks(longPressRunnable)
        activeTextView = null
        pressedSpan = null
        longPressTriggered = false
    }
}

private fun findClickableSpan(textView: TextView, event: MotionEvent): ClickableSpan? {
    val text = textView.text as? Spanned ?: return null
    val layout = textView.layout ?: return null
    val x = event.x.toInt() - textView.totalPaddingLeft + textView.scrollX
    val y = event.y.toInt() - textView.totalPaddingTop + textView.scrollY

    if (x < 0 || y < 0 || x > layout.width || y > layout.height) {
        return null
    }

    val line = layout.getLineForVertical(y)
    val offset = layout.getOffsetForHorizontal(line, x.toFloat())
    return text.getSpans(offset, offset, ClickableSpan::class.java)
        .firstOrNull { span ->
            val spanStart = text.getSpanStart(span)
            val spanEnd = text.getSpanEnd(span)
            offset in spanStart until spanEnd
        }
}

private fun copyTextToClipboard(context: Context, text: CharSequence): Boolean {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("reply_content", text))
    Toast.makeText(context, context.getString(R.string.reply_text_copied), Toast.LENGTH_SHORT).show()
    return true
}
