package com.example.xddemo.data.repository

import com.example.xddemo.reply
import com.example.xddemo.replyEntity
import com.example.xddemo.threadEntity
import com.example.xddemo.threadPage
import com.example.xddemo.data.dao.ThreadDao
import com.example.xddemo.data.model.ApiResult
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.toReplyEntity
import com.example.xddemo.data.model.toThreadEntity
import com.example.xddemo.network.XDaoApiService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ThreadRepositoryTest {

    private val xDaoApiService = mockk<XDaoApiService>()
    private val threadDao = mockk<ThreadDao>()

    private lateinit var repository: ThreadRepository

    @Before
    fun setUp() {
        repository = ThreadRepository(xDaoApiService, threadDao)
    }

    @Test
    fun saveAllThreadPage_savesAllPages_filtersPlaceholderReplies_andMarksDownloaded() = runTest {
        val threadId = 100
        val pageOne = threadPage(
            id = threadId,
            replyCount = 20,
            replies = listOf(reply(id = 1), reply(id = 9999999), reply(id = 2))
        )
        val pageTwo = threadPage(
            id = threadId,
            replyCount = 20,
            replies = listOf(reply(id = 3))
        )
        val insertedReplies = mutableListOf<List<ReplyEntity>>()

        coEvery { threadDao.getSingleThread(threadId) } returns null
        coEvery { xDaoApiService.getThreadPage(threadId, 1) } returns pageOne
        coEvery { xDaoApiService.getThreadPage(threadId, 2) } returns pageTwo
        coEvery { threadDao.insertThread(any()) } just Runs
        coEvery { threadDao.insertReplies(any()) } answers {
            @Suppress("UNCHECKED_CAST")
            insertedReplies += args[0] as List<ReplyEntity>
            Unit
        }
        coEvery { threadDao.updateDownloadStatus(threadId, any()) } just Runs

        val result = repository.saveAllThreadPage(threadId)

        assertTrue(result is ApiResult.Success)
        assertEquals(listOf(1, 2, 3), insertedReplies.flatten().map { it.id })
        assertEquals(listOf(threadId, threadId, threadId), insertedReplies.flatten().map { it.threadId })
        coVerify(exactly = 1) { threadDao.insertThread(pageOne.toThreadEntity()) }
        coVerify(exactly = 2) { xDaoApiService.getThreadPage(threadId, 1) }
        coVerify(exactly = 1) { xDaoApiService.getThreadPage(threadId, 2) }
        coVerify(exactly = 1) { threadDao.updateDownloadStatus(threadId, true) }
    }

    @Test
    fun saveAllThreadPage_returnsError_whenApiThrows() = runTest {
        val threadId = 101

        coEvery { threadDao.getSingleThread(threadId) } returns null
        coEvery { xDaoApiService.getThreadPage(threadId, 1) } throws IllegalStateException("boom")

        val result = repository.saveAllThreadPage(threadId)

        assertTrue(result is ApiResult.Error)
        assertEquals("boom", (result as ApiResult.Error).message)
        coVerify(exactly = 0) { threadDao.insertThread(any()) }
        coVerify(exactly = 0) { threadDao.updateDownloadStatus(threadId, any()) }
    }

    @Test
    fun updateThread_fetchesOnlyNewPages_andMarksDownloaded() = runTest {
        val threadId = 102
        val latestHead = threadPage(id = threadId, replyCount = 40)
        val pageTwo = threadPage(
            id = threadId,
            replyCount = 40,
            replies = listOf(reply(id = 20), reply(id = 21))
        )
        val pageThree = threadPage(
            id = threadId,
            replyCount = 40,
            replies = listOf(reply(id = 39), reply(id = 40))
        )
        val insertedReplies = mutableListOf<List<ReplyEntity>>()

        coEvery { threadDao.updateDownloadStatus(threadId, any()) } just Runs
        coEvery { xDaoApiService.getThreadPage(threadId, 1) } returns latestHead
        coEvery { xDaoApiService.getThreadPage(threadId, 2) } returns pageTwo
        coEvery { xDaoApiService.getThreadPage(threadId, 3) } returns pageThree
        coEvery { threadDao.updateThread(latestHead.toThreadEntity()) } just Runs
        coEvery { threadDao.getReplyCountByThreadId(threadId) } returns 19
        coEvery { threadDao.insertReplies(any()) } answers {
            @Suppress("UNCHECKED_CAST")
            insertedReplies += args[0] as List<ReplyEntity>
            Unit
        }

        val result = repository.updateThread(threadId)

        assertTrue(result is ApiResult.Success)
        assertEquals(listOf(20, 21, 39, 40), insertedReplies.flatten().map { it.id })
        coVerify(exactly = 1) { xDaoApiService.getThreadPage(threadId, 1) }
        coVerify(exactly = 1) { xDaoApiService.getThreadPage(threadId, 2) }
        coVerify(exactly = 1) { xDaoApiService.getThreadPage(threadId, 3) }
        coVerify(exactly = 1) { threadDao.updateDownloadStatus(threadId, false) }
        coVerify(exactly = 1) { threadDao.updateDownloadStatus(threadId, true) }
    }

    @Test
    fun getAllThreads_returnsOnlyDownloadedThreads() = runTest {
        val downloadedThread = threadEntity(id = 1, isDownloaded = true)
        val pendingThread = threadEntity(id = 2, isDownloaded = false)

        every { threadDao.getAllThreads() } returns flowOf(listOf(downloadedThread, pendingThread))

        val result = repository.getAllThreads().first()

        assertEquals(listOf(downloadedThread), result)
    }

    @Test
    fun getSingleReply_returnsThreadAsReply_whenThreadExists() = runTest {
        val thread = threadEntity(id = 103)

        coEvery { threadDao.getSingleThread(thread.id) } returns thread

        val result = repository.getSingleReply(thread.id)

        assertEquals(thread.toReplyEntity(), result)
        coVerify(exactly = 0) { threadDao.getSingleReply(any()) }
    }

    @Test
    fun searchByKeywords_combinesThreadAndReplyMatches() = runTest {
        val thread = threadEntity(id = 104, content = "contains-keyword")
        val reply = replyEntity(id = 204, threadId = 104, content = "reply-keyword")

        coEvery { threadDao.searchThreadsByContent("keyword") } returns listOf(thread)
        coEvery { threadDao.searchRepliesByContent("keyword") } returns listOf(reply)

        val result = repository.searchByKeywords("keyword")

        assertEquals(listOf(thread.toReplyEntity(), reply), result)
    }
}
