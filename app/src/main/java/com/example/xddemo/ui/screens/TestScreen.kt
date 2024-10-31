package com.example.xddemo.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.components.PageSliderDialogButton
import com.example.xddemo.ui.theme.MyApplicationTheme
import com.example.xddemo.ui.viewmodel.ThreadViewModel


@Composable
fun TestScreen(
    viewModel: ThreadViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    PageSliderDialogButton(
        initialPage = 4,
        totalPage = 10,
        {}
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "开发中",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Center),
            color = Color.Gray
        )
    }

}

@Preview
@Composable
fun TestScreenPreview() {
    MyApplicationTheme {
        TestScreen()
    }
}