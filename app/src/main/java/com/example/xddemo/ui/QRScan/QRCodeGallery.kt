package com.example.qrdemo.QRScan

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.json.JSONObject
import java.io.InputStream

@Composable
fun QrCodeFromGallery(
    saveCookie: (String) -> Unit
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var scannedResult by remember { mutableStateOf<String?>(null) }

    // 图片选择器
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE) // 强制使用软件解码，避免HARDWARE配置
                }
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Select QR Code from Gallery")
        }

        bitmap?.let {
            // 扫描二维码
            val result = scanQrCodeFromBitmap(it)
            result?.let { qrCode ->
                Text("QR Code: $qrCode")
                scannedResult = qrCode
                val cookie = JSONObject(qrCode).optString("cookie")
                saveCookie("userhash=$cookie")
                Log.d("QrCodeGallery", "cookie Result: $cookie")
            } ?: Text("No QR Code found")
        }
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
