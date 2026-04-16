package com.example.qrdemo.QRScan

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.example.xddemo.R
import com.google.zxing.integration.android.IntentIntegrator

@Composable
fun rememberQrCodeScannerLauncher(
    saveCookie: (String) -> Unit,
    onImportResult: (Int) -> Unit
): () -> Unit {
    val activity = LocalContext.current as? Activity
    val currentSaveCookie by rememberUpdatedState(saveCookie)
    val currentOnImportResult by rememberUpdatedState(onImportResult)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult = IntentIntegrator.parseActivityResult(
            result.resultCode, result.data
        )
        val scannedResult = intentResult?.contents
        if (scannedResult != null && scannedResult != "Cancelled") {
            val userHash = extractUserHash(scannedResult)
            if (userHash != null) {
                currentSaveCookie(userHash)
                currentOnImportResult(R.string.import_cookie_success)
                Log.d("QrCodeScanner", "cookie Result: $userHash")
            } else {
                currentOnImportResult(R.string.import_cookie_invalid)
            }
        }
    }

    return {
        activity?.let { currentActivity ->
            val integrator = IntentIntegrator(currentActivity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setBeepEnabled(true)
            launcher.launch(integrator.createScanIntent())
        }
    }
}

internal fun extractUserHash(qrContent: String): String? {
    val cookie = runCatching {
        org.json.JSONObject(qrContent).optString("cookie")
    }.getOrNull().orEmpty()

    return cookie.takeIf { it.isNotBlank() }?.let { "userhash=$it" }
}
