package com.example.xddemo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SearchButtonWithDialog(
    navigateToResult: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }  // 控制Dialog的显示状态
    var keywords by remember { mutableStateOf("") }

    // IconButton 点击后显示 AlertDialog
    IconButton(onClick = { showDialog = true }) {
        Icon(imageVector = Filled.Search, contentDescription = "Search")
    }

    // AlertDialog 弹出
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            // Draw a rectangle shape with rounded corners inside the dialog
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
                        text = "搜索关键词",
                        modifier = Modifier.padding(start = 18.dp, top = 12.dp),
                    )
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        TextField(
                            value = keywords,
                            onValueChange = { keywords = it },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent
                            ),
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
                                navigateToResult(keywords)
                            },
                            modifier = Modifier.padding(top = 8.dp, end = 6.dp),
                        ) {
                            Text("搜索")
                        }
                    }
                }
            }
        }
    }
}