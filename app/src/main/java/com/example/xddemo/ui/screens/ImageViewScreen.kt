package com.example.xddemo.ui.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.core.content.ContextCompat
import coil3.compose.rememberAsyncImagePainter
import com.example.xddemo.R
import com.jvziyaoyao.scale.zoomable.zoomable.ZoomableView
import com.jvziyaoyao.scale.zoomable.zoomable.rememberZoomableState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ImageViewScreen(
    url: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val painter = rememberAsyncImagePainter(model = url)
    val state = rememberZoomableState(contentSize = painter.intrinsicSize)
    var isSaving by remember { mutableStateOf(false) }

    fun saveImage() {
        if (isSaving) return

        coroutineScope.launch {
            isSaving = true
            val toastMessage = try {
                val fileName = saveImageToPictures(context = context, url = url)
                context.getString(R.string.image_save_success, fileName)
            } catch (_: Exception) {
                context.getString(R.string.image_save_failed)
            }

            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            isSaving = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            saveImage()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.image_save_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

        Surface(
            color = Color(0x66000000),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || hasLegacyWritePermission(context)) {
                        saveImage()
                    } else {
                        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                },
                enabled = !isSaving,
            ) {
                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = stringResource(R.string.cd_download),
                    tint = Color.White,
                )
            }
        }
    }
}

private suspend fun saveImageToPictures(context: Context, url: String): String = withContext(Dispatchers.IO) {
    val request = Request.Builder()
        .url(url)
        .build()

    OkHttpClient().newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            throw IOException("Unexpected response ${response.code}")
        }

        val responseBody = response.body ?: throw IOException("Empty response body")
        val mimeType = responseBody.contentType()?.toString()
            ?: "image/*"
        val baseFileName = URLUtil.guessFileName(url, response.header("Content-Disposition"), mimeType)
        val fileName = buildSavedFileName(baseFileName)

        responseBody.byteStream().use { inputStream ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImageWithMediaStore(
                    context = context,
                    fileName = fileName,
                    mimeType = mimeType,
                    inputStream = inputStream,
                )
            } else {
                saveImageToLegacyPictures(
                    context = context,
                    fileName = fileName,
                    mimeType = mimeType,
                    inputStream = inputStream,
                )
            }
        }

        fileName
    }
}

private fun saveImageWithMediaStore(
    context: Context,
    fileName: String,
    mimeType: String,
    inputStream: java.io.InputStream,
) {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/chaDao_pic")
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        ?: throw IOException("Failed to create media store record")

    try {
        resolver.openOutputStream(uri)?.use { outputStream ->
            inputStream.copyTo(outputStream)
        } ?: throw IOException("Failed to open output stream")

        values.clear()
        values.put(MediaStore.MediaColumns.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    } catch (exception: Exception) {
        resolver.delete(uri, null, null)
        throw exception
    }
}

private fun saveImageToLegacyPictures(
    context: Context,
    fileName: String,
    mimeType: String,
    inputStream: java.io.InputStream,
) {
    val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val targetDirectory = File(picturesDirectory, "chaDao_pic")

    if (!targetDirectory.exists() && !targetDirectory.mkdirs()) {
        throw IOException("Failed to create target directory")
    }

    val targetFile = File(targetDirectory, fileName)
    FileOutputStream(targetFile).use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    MediaScannerConnection.scanFile(
        context,
        arrayOf(targetFile.absolutePath),
        arrayOf(mimeType),
        null,
    )
}

private fun buildSavedFileName(baseFileName: String): String {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val dotIndex = baseFileName.lastIndexOf('.')

    return if (dotIndex > 0) {
        val name = baseFileName.substring(0, dotIndex)
        val extension = baseFileName.substring(dotIndex)
        "${name}_${timestamp}${extension}"
    } else {
        "${baseFileName}_${timestamp}"
    }
}

private fun hasLegacyWritePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    ) == PackageManager.PERMISSION_GRANTED
}
