package com.example.qrdemo.QRScan


import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.xddemo.R
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject

@Composable
fun QrCodeScanner(
    saveCookie: (String) -> Unit
) {
    val context = LocalContext.current as Activity
    var scannedResult by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult = IntentIntegrator.parseActivityResult(
            result.resultCode, result.data
        )
        scannedResult = intentResult?.contents
        if (scannedResult != null && scannedResult != "Cancelled") {
            val cookie = JSONObject(scannedResult).optString("cookie")
            saveCookie("userhash=$cookie")
            Log.d("QrCodeScanner", "cookie Result: $cookie")
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(onClick = {
            val integrator = IntentIntegrator(context)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            //integrator.setPrompt("Scan a QR code")
            integrator.setBeepEnabled(true)
            launcher.launch(integrator.createScanIntent())
        }) {
            Text(stringResource(R.string.qr_scan_button))
        }

        scannedResult?.let {
            Text(stringResource(R.string.qr_scan_result, it))
        }
    }
}
