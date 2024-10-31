package com.example.xddemo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.xddemo.ui.theme.MyApplicationTheme

@Composable
fun DownloadButtonWithDialog(
    onConfirmation: (Int) -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(false) }  // 控制Dialog的显示状态
    var threadId by remember { mutableStateOf("") }

    // IconButton 点击后显示 AlertDialog
    IconButton(onClick = { showDialog = true }) {
        Icon(imageVector = Filled.Download, contentDescription = "Download")
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Text(
                        text = "输入串号",
                        modifier = Modifier.padding(start = 18.dp, top = 12.dp),
                    )
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        TextField(
                            value = threadId,
                            onValueChange = { threadId = it },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                                onConfirmation(threadId.toInt())
                            },
                            modifier = Modifier.padding(top = 8.dp, end = 6.dp),
                        ) {
                            Text("下载")
                        }
                    }
                }
            }
        }
    }

}


@Preview
@Composable
fun DownloadDialogPreview() {
    MyApplicationTheme {
        DownloadButtonWithDialog()
    }
}