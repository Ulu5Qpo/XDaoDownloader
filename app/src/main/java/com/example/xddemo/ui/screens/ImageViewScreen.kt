package com.example.xddemo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.viewmodel.ThreadViewModel
import com.jvziyaoyao.scale.zoomable.zoomable.ZoomableView
import com.jvziyaoyao.scale.zoomable.zoomable.rememberZoomableState

@Composable
fun ImageViewScreen(
    url: String
) {
    val painter = rememberAsyncImagePainter(model = url)
    val state = rememberZoomableState(contentSize = painter.intrinsicSize)

    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        ZoomableView(state = state) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = null
            )
        }
    }

}