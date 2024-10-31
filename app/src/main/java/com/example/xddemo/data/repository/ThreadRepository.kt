package com.example.xddemo.data.repository

import android.util.Log
import com.example.xddemo.data.dao.ThreadDao
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity
import com.example.xddemo.data.model.ThreadWithReplies
import com.example.xddemo.data.model.toReplyEntity
import com.example.xddemo.data.model.toThreadEntity
import com.example.xddemo.network.XDaoApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.ceil
import kotlin.math.max


class ThreadRepository(
    private val xDaoApiService: XDaoApiService,
    private val threadDao: ThreadDao
) {
    private suspend fun saveOneThreadPage(id: Int, page: Int = 1) {
        val threadPage = xDaoApiService.getThreadPage(id = id, page = page)
        val threadEntity = threadPage.toThreadEntity()
        val replies = threadPage.replies
            .filter { reply -> reply.id != 9999999 }
            .map { reply -> reply.toReplyEntity().copy(threadId = threadEntity.id) }
        threadDao.insertReplies(replies)
    }

    suspend fun saveAllThreadPage(id: Int) {
        if (threadDao.getSingleThread(id) != null) {
            updateThread(id)
            return
        }
        val threadPage = xDaoApiService.getThreadPage(id = id, page = 1)
        threadDao.insertThread(threadPage.toThreadEntity())
        val threadPageCount = max(1, ceil(threadPage.replyCount / 19.0).toInt())
        for (page in 1..threadPageCount) {
            saveOneThreadPage(id, page)
            delay(500L)
        }
        threadDao.updateDownloadStatus(id, true)
    }

    suspend fun updateThread(id: Int) {
        threadDao.updateDownloadStatus(id, false)
        val newerThreadPage = xDaoApiService.getThreadPage(id = id, page = 1)
        threadDao.updateThread(newerThreadPage.toThreadEntity())
        val maxThreadPage = max(1, ceil(newerThreadPage.replyCount / 19.0).toInt())
        val newStartIndex = threadDao.getReplyCountByThreadId(id) + 1
        val newStartPage = max(1, ceil(newStartIndex / 19.0).toInt())
        Log.d("updateThreadInRepository","newStartPage: $newStartPage maxThreadPage: $maxThreadPage")
        for (page in newStartPage..maxThreadPage) {
            saveOneThreadPage(id, page)
            Log.d("updateThreadInRepository","curPage: $page")
            delay(500L)
        }
        threadDao.updateDownloadStatus(id, true)
    }

    fun getThreadWithReplies(threadId: Int): Flow<ThreadWithReplies> {
        return threadDao.getThreadWithReplies(threadId)
    }

    fun getAllThreads(): Flow<List<ThreadEntity>> {
        return threadDao.getAllThreads().map { thread ->
            thread.filter { it.isDownloaded }
        }
    }

    suspend fun getSingleReply(replyId: Int): ReplyEntity? {
        if (threadDao.getSingleThread(replyId) != null) {
            return threadDao.getSingleThread(replyId)!!.toReplyEntity()
        }
        return threadDao.getSingleReply(replyId)
    }

    suspend fun searchByKeywords(keywords: String): List<ReplyEntity> {
        return threadDao.searchThreadsByContent(keywords)
            .map { it.toReplyEntity() } + threadDao.searchRepliesByContent(keywords)
    }

    suspend fun deleteThread(threadEntity: ThreadEntity) {
        return threadDao.deleteThread(threadEntity)
    }
}