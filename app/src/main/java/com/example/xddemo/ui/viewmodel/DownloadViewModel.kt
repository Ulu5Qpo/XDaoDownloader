package com.example.xddemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xddemo.data.repository.ThreadRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class DownloadStatus {
    DOWNLOADING,
    UPDATING,
    COMPLETE
}

data class DownloadState(
    val threadId: Int,
    val status: DownloadStatus = DownloadStatus.DOWNLOADING
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

            repository.saveAllThreadPage(id)
            _downloads.value = _downloads.value.map {
                if (it.threadId == id) it.copy(status = DownloadStatus.COMPLETE) else it
            }
        }
    }

    fun startUpdate(id: Int) {
        val newDownload = DownloadState(id, DownloadStatus.UPDATING)
        _downloads.value += newDownload

        viewModelScope.launch {
            repository.updateThread(id)
            _downloads.value = _downloads.value.map {
                if (it.threadId == id) it.copy(status = DownloadStatus.COMPLETE) else it
            }
        }
    }

}