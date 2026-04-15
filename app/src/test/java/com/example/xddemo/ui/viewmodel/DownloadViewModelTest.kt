package com.example.xddemo.ui.viewmodel

import android.app.Application
import com.example.xddemo.R
import com.example.xddemo.replyEntity
import com.example.xddemo.data.model.ApiResult
import com.example.xddemo.data.repository.ThreadRepository
import com.example.xddemo.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DownloadViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<ThreadRepository>()
    private val application = mockk<Application>()

    @Test
    fun startDownload_marksTaskComplete_whenSaveSucceeds() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { repository.getSingleReply(1) } returns null
        coEvery { repository.saveAllThreadPage(1) } returns ApiResult.Success(Unit)

        val viewModel = DownloadViewModel(repository, application)
        viewModel.startDownload(1)
        advanceUntilIdle()

        assertEquals(listOf(DownloadState(1, DownloadStatus.COMPLETE)), viewModel.downloadList.value)
    }

    @Test
    fun startDownload_usesLocalizedNetworkFallback_whenSaveFailsWithoutMessage() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { repository.getSingleReply(2) } returns null
        coEvery { repository.saveAllThreadPage(2) } returns ApiResult.Error("")
        every { application.getString(R.string.error_network) } returns "网络错误"
        every { application.getString(R.string.error_download_failed, "网络错误") } returns "下载失败: 网络错误"

        val viewModel = DownloadViewModel(repository, application)
        viewModel.startDownload(2)
        advanceUntilIdle()

        assertEquals(
            listOf(DownloadState(2, DownloadStatus.ERROR, "下载失败: 网络错误")),
            viewModel.downloadList.value
        )
    }

    @Test
    fun startDownload_switchesToUpdate_whenReplyAlreadyExists() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { repository.getSingleReply(3) } returns replyEntity(id = 3)
        coEvery { repository.updateThread(3) } returns ApiResult.Success(Unit)

        val viewModel = DownloadViewModel(repository, application)
        viewModel.startDownload(3)
        advanceUntilIdle()

        assertEquals(listOf(DownloadState(3, DownloadStatus.COMPLETE)), viewModel.downloadList.value)
        coVerify(exactly = 0) { repository.saveAllThreadPage(any()) }
        coVerify(exactly = 1) { repository.updateThread(3) }
    }

    @Test
    fun clearError_removesOnlyErrorItems() = runTest(mainDispatcherRule.testDispatcher) {
        coEvery { repository.getSingleReply(4) } returns null
        coEvery { repository.getSingleReply(5) } returns null
        coEvery { repository.saveAllThreadPage(4) } returns ApiResult.Error("timeout")
        coEvery { repository.saveAllThreadPage(5) } returns ApiResult.Success(Unit)
        every { application.getString(R.string.error_download_failed, "timeout") } returns "下载失败: timeout"

        val viewModel = DownloadViewModel(repository, application)
        viewModel.startDownload(4)
        viewModel.startDownload(5)
        advanceUntilIdle()
        viewModel.clearError(4)

        assertEquals(listOf(DownloadState(5, DownloadStatus.COMPLETE)), viewModel.downloadList.value)
    }
}
