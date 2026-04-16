package com.example.qrdemo.QRScan

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.example.xddemo.R
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

@Composable
fun rememberQrCodeFromGalleryLauncher(
    saveCookie: (String) -> Unit,
    onImportResult: (Int) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val currentSaveCookie by rememberUpdatedState(saveCookie)
    val currentOnImportResult by rememberUpdatedState(onImportResult)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE)
                }
            }

            val qrCode = scanQrCodeFromBitmap(bitmap)
            if (qrCode == null) {
                currentOnImportResult(R.string.import_cookie_not_found)
                return@rememberLauncherForActivityResult
            }

            val userHash = extractUserHash(qrCode)
            if (userHash != null) {
                currentSaveCookie(userHash)
                currentOnImportResult(R.string.import_cookie_success)
                Log.d("QrCodeGallery", "cookie Result: $userHash")
            } else {
                currentOnImportResult(R.string.import_cookie_invalid)
            }
        }
    }

    return {
        galleryLauncher.launch("image/*")
    }
}

fun scanQrCodeFromBitmap(bitmap: Bitmap): String? {
    val intArray = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
    val reader = MultiFormatReader()

    return try {
        val result = reader.decode(binaryBitmap)
        result.text
    } catch (e: Exception) {
        null
    }
}
