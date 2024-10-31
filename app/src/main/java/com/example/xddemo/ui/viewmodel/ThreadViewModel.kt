package com.example.xddemo.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity
import com.example.xddemo.data.model.ThreadWithReplies
import com.example.xddemo.data.repository.ThreadRepository
import com.example.xddemo.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThreadViewModel(
    private val repository: ThreadRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val allThreads: StateFlow<AllThreadState> =
        repository.getAllThreads().map { AllThreadState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllThreadState()
            )

    private val _threadWithReplies = MutableStateFlow<ThreadWithReplies?>(null)
    val threadWithReplies: StateFlow<ThreadWithReplies?> = _threadWithReplies

    fun getThreadWithReplies(threadId: Int) {
        viewModelScope.launch {
            repository.getThreadWithReplies(threadId).collect { threadWithReplies ->
                _threadWithReplies.value = threadWithReplies
            }
        }
    }

    suspend fun getSingleReply(replyId: Int): ReplyEntity? {
        return repository.getSingleReply(replyId)
    }

    private val _searchResults = MutableStateFlow<List<ReplyEntity>>(emptyList())
    val searchResults: StateFlow<List<ReplyEntity>> = _searchResults

    fun searchByKeywords(keywords: String) {
        viewModelScope.launch {
            _searchResults.value = repository.searchByKeywords(keywords)
        }
    }

    fun deleteThread() {
        viewModelScope.launch {
            _threadWithReplies.value?.let { repository.deleteThread(it.thread) }
        }
    }

    fun saveCookie(cookie: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserHash(cookie)
        }
    }

}

data class AllThreadState(val itemList: List<ThreadEntity> = listOf())
