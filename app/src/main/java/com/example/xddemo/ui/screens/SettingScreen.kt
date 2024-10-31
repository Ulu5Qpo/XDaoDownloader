package com.example.xddemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrdemo.QRScan.QrCodeFromGallery
import com.example.qrdemo.QRScan.QrCodeScanner
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.theme.MyApplicationTheme
import com.example.xddemo.ui.viewmodel.ThreadViewModel

@Composable
fun SettingScreen(
    viewModel: ThreadViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Column {
        QrCodeScanner(saveCookie = { viewModel.saveCookie(it) })
        QrCodeFromGallery(saveCookie = { viewModel.saveCookie(it) })
    }


}

@Preview
@Composable
fun QRScreenPreview() {
    MyApplicationTheme {
        SettingScreen()
    }
}