package com.example.xddemo.ui.viewmodel

import android.app.Application
import com.example.xddemo.R
import com.example.xddemo.replyEntity
import com.example.xddemo.threadEntity
import com.example.xddemo.threadWithReplies
import com.example.xddemo.data.repository.ThreadRepository
import com.example.xddemo.data.repository.UserPreferencesRepository
import com.example.xddemo.testutil.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThreadViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<ThreadRepository>()
    private val userPreferencesRepository = mockk<UserPreferencesRepository>()
    private val application = mockk<Application>()

    @Test
    fun searchByKeywords_updatesResults_onSuccess() = runTest(mainDispatcherRule.testDispatcher) {
        val reply = replyEntity(id = 10)

        every { repository.getAllThreads() } returns flowOf(emptyList())
        coEvery { repository.searchByKeywords("keyword") } returns listOf(reply)

        val viewModel = ThreadViewModel(repository, userPreferencesRepository, application)
        viewModel.searchByKeywords("keyword")
        advanceUntilIdle()

        assertEquals(listOf(reply), viewModel.searchResults.value)
        assertNull(viewModel.searchError.value)
    }

    @Test
    fun searchByKeywords_setsLocalizedError_whenSearchFails() = runTest(mainDispatcherRule.testDispatcher) {
        every { repository.getAllThreads() } returns flowOf(emptyList())
        coEvery { repository.searchByKeywords("keyword") } throws IllegalStateException()
        every { application.getString(R.string.error_unknown) } returns "未知错误"
        every { application.getString(R.string.error_search_failed, "未知错误") } returns "搜索失败: 未知错误"

        val viewModel = ThreadViewModel(repository, userPreferencesRepository, application)
        viewModel.searchByKeywords("keyword")
        advanceUntilIdle()

        assertEquals("搜索失败: 未知错误", viewModel.searchError.value)
        assertTrue(viewModel.searchResults.value.isEmpty())
    }

    @Test
    fun getThreadWithReplies_collectsRepositoryFlow() = runTest(mainDispatcherRule.testDispatcher) {
        val thread = threadEntity(id = 11)
        val reply = replyEntity(id = 12, threadId = 11)
        val expected = threadWithReplies(thread, listOf(reply))

        every { repository.getAllThreads() } returns flowOf(emptyList())
        every { repository.getThreadWithReplies(11) } returns flowOf(expected)

        val viewModel = ThreadViewModel(repository, userPreferencesRepository, application)
        viewModel.getThreadWithReplies(11)
        advanceUntilIdle()

        assertEquals(expected, viewModel.threadWithReplies.value)
    }

    @Test
    fun deleteThread_deletesCurrentLoadedThread() = runTest(mainDispatcherRule.testDispatcher) {
        val thread = threadEntity(id = 13)
        val expected = threadWithReplies(thread, emptyList())

        every { repository.getAllThreads() } returns flowOf(emptyList())
        every { repository.getThreadWithReplies(13) } returns flowOf(expected)
        coEvery { repository.deleteThread(thread) } just Runs

        val viewModel = ThreadViewModel(repository, userPreferencesRepository, application)
        viewModel.getThreadWithReplies(13)
        advanceUntilIdle()
        viewModel.deleteThread()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.deleteThread(thread) }
    }

    @Test
    fun saveCookie_delegatesToUserPreferencesRepository() = runTest(mainDispatcherRule.testDispatcher) {
        every { repository.getAllThreads() } returns flowOf(emptyList())
        coEvery { userPreferencesRepository.saveUserHash("cookie") } just Runs

        val viewModel = ThreadViewModel(repository, userPreferencesRepository, application)
        viewModel.saveCookie("cookie")
        advanceUntilIdle()

        coVerify(exactly = 1) { userPreferencesRepository.saveUserHash("cookie") }
    }
}
