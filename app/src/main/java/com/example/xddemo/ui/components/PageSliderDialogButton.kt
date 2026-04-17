package com.example.xddemo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.UTurnRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.xddemo.R
import com.example.xddemo.ui.theme.MyApplicationTheme

@Composable
fun PageSliderDialogButton(
    initialPage: Int,
    totalPage: Int,
    onPageSelected: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectPage by remember { mutableIntStateOf(initialPage) }
    val safeTotalPage = totalPage.coerceAtLeast(1)
    val safeSelectedPage = selectPage.coerceIn(1, safeTotalPage)

    IconButton(onClick = {
        selectPage = initialPage.coerceIn(1, safeTotalPage)
        showDialog = true
    }) {
        Icon(imageVector = Filled.UTurnRight, null)
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Text(
                        text = stringResource(R.string.page_jump_title),
                        modifier = Modifier.padding(start = 18.dp, top = 12.dp),
                    )
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Slider(
                            value = safeSelectedPage.toFloat(),
                            onValueChange = {
                                selectPage = it.toInt().coerceIn(1, safeTotalPage)
                            },
                            valueRange = 1f..safeTotalPage.toFloat(),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "$safeSelectedPage/$safeTotalPage",
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = {
                                showDialog = false
                                onPageSelected(safeSelectedPage)
                            },
                            modifier = Modifier.padding(end = 6.dp),
                        ) {
                            Text(stringResource(R.string.action_confirm))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PageSliderDialogPreview() {
    MyApplicationTheme {
        PageSliderDialogButton(4, 10) {}
    }
}
