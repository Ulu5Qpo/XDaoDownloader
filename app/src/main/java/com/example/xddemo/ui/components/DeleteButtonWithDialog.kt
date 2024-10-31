package com.example.xddemo.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DeleteButtonWithDialog(
    onConfirmAction: () -> Unit,
    navigateUp: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }  // 控制Dialog的显示状态

    // IconButton 点击后显示 AlertDialog
    IconButton(onClick = { showDialog = true }) {
        Icon(imageVector = Filled.DeleteForever, contentDescription = "Delete")
    }

    // AlertDialog 弹出
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 点击空白处关闭 Dialog
            title = { Text("删除") },
            text = { Text("确认要删除该串吗") },
            confirmButton = {
                TextButton(onClick = {
                    onConfirmAction()  // 执行传入的函数
                    showDialog = false // 关闭 Dialog
                    navigateUp()
                }) {
                    Text("确认删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}