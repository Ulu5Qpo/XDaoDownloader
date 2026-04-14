package com.example.xddemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xddemo.data.model.ApiResult
import com.example.xddemo.data.repository.ThreadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class DownloadStatus {
    DOWNLOADING,
    UPDATING,
    COMPLETE,
    ERROR
}

data class DownloadState(
    val threadId: Int,
    val status: DownloadStatus = DownloadStatus.DOWNLOADING,
    val errorMessage: String = ""
)

class DownloadViewModel(
    private val repository: ThreadRepository,
) : ViewModel() {

    private val _downloads = MutableStateFlow<List<DownloadState>>(emptyList())
    val downloadList: StateFlow<List<DownloadState>> = _downloads

    fun startDownload(id: Int) {
        viewModelScope.launch {
            if (repository.getSingleReply(id) != null) {
                startUpdate(id)
                return@launch
            }

            val newDownload = DownloadState(id)
            _downloads.value += newDownload

            when (val result = repository.saveAllThreadPage(id)) {
                is ApiResult.Success -> {
                    _downloads.value = _downloads.value.map {
                        if (it.threadId == id) it.copy(status = DownloadStatus.COMPLETE) else it
                    }
                }
                is ApiResult.Error -> {
                    _downloads.value = _downloads.value.map {
                        if (it.threadId == id) it.copy(status = DownloadStatus.ERROR, errorMessage = result.message) else it
                    }
                }
            }
        }
    }

    fun startUpdate(id: Int) {
        val newDownload = DownloadState(id, DownloadStatus.UPDATING)
        _downloads.value += newDownload

        viewModelScope.launch {
            when (val result = repository.updateThread(id)) {
                is ApiResult.Success -> {
                    _downloads.value = _downloads.value.map {
                        if (it.threadId == id) it.copy(status = DownloadStatus.COMPLETE) else it
                    }
                }
                is ApiResult.Error -> {
                    _downloads.value = _downloads.value.map {
                        if (it.threadId == id) it.copy(status = DownloadStatus.ERROR, errorMessage = result.message) else it
                    }
                }
            }
        }
    }

    fun clearError(threadId: Int) {
        _downloads.value = _downloads.value.mapNotNull {
            if (it.threadId == threadId && it.status == DownloadStatus.ERROR) null else it
        }
    }

}
