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
import androidx.compose.ui.res.stringResource
import com.example.xddemo.R

@Composable
fun DeleteButtonWithDialog(
    onConfirmAction: () -> Unit,
    navigateUp: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }  // 控制Dialog的显示状态

    // IconButton 点击后显示 AlertDialog
    IconButton(onClick = { showDialog = true }) {
        Icon(imageVector = Filled.DeleteForever, contentDescription = stringResource(R.string.cd_delete))
    }

    // AlertDialog 弹出
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 点击空白处关闭 Dialog
            title = { Text(stringResource(R.string.delete_title)) },
            text = { Text(stringResource(R.string.delete_thread_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    onConfirmAction()  // 执行传入的函数
                    showDialog = false // 关闭 Dialog
                    navigateUp()
                }) {
                    Text(stringResource(R.string.action_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }
}
