package com.example.xddemo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.xddemo.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrdemo.QRScan.rememberQrCodeFromGalleryLauncher
import com.example.qrdemo.QRScan.rememberQrCodeScannerLauncher
import com.example.xddemo.ui.AppViewModelProvider
import com.example.xddemo.ui.theme.MyApplicationTheme
import com.example.xddemo.ui.viewmodel.ThreadViewModel

@Composable
fun SettingScreen(
    navigateBack: () -> Unit,
    viewModel: ThreadViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val showImportToast: (Int) -> Unit = { messageRes ->
        Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
    }

    val launchQrCodeScanner = rememberQrCodeScannerLauncher(
        saveCookie = viewModel::saveCookie,
        onImportResult = showImportToast
    )
    val launchGalleryQrCodeScanner =
        rememberQrCodeFromGalleryLauncher(
            saveCookie = viewModel::saveCookie,
            onImportResult = showImportToast
        )

    SettingScreenContent(
        onNavIconClick = navigateBack,
        onScanQrCodeClick = launchQrCodeScanner,
        onPickQrCodeFromGalleryClick = launchGalleryQrCodeScanner
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SettingScreenContent(
    onNavIconClick: () -> Unit,
    onScanQrCodeClick: () -> Unit,
    onPickQrCodeFromGalleryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavIconClick) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = stringResource(R.string.settings_import_cookie),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                ListItem(
                    modifier = Modifier.clickable(onClick = onScanQrCodeClick),
                    icon = {
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                    },
                    text = {
                        Text(text = stringResource(R.string.settings_scan_qr_code))
                    }
                )
                Divider()
                ListItem(
                    modifier = Modifier.clickable(onClick = onPickQrCodeFromGalleryClick),
                    icon = {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                    },
                    text = {
                        Text(text = stringResource(R.string.settings_pick_qr_from_gallery))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun QRScreenPreview() {
    MyApplicationTheme {
        SettingScreenContent(
            onNavIconClick = {},
            onScanQrCodeClick = {},
            onPickQrCodeFromGalleryClick = {}
        )
    }
}
