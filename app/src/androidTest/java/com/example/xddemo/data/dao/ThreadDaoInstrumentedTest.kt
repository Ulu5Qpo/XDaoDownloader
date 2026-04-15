package com.example.xddemo.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.xddemo.data.XDaoDatabase
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThreadDaoInstrumentedTest {

    private lateinit var database: XDaoDatabase
    private lateinit var threadDao: ThreadDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, XDaoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        threadDao = database.threadDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun getThreadWithReplies_returnsInsertedRelation() = runBlocking {
        val thread = threadEntity(id = 1, content = "thread keyword")
        val replies = listOf(
            replyEntity(id = 11, threadId = 1, content = "first reply"),
            replyEntity(id = 12, threadId = 1, content = "second reply")
        )

        threadDao.insertThread(thread)
        threadDao.insertReplies(replies)

        val result = threadDao.getThreadWithReplies(1).first()

        assertEquals(thread, result.thread)
        assertEquals(replies, result.replies)
    }

    @Test
    fun searchAndDeleteThread_supportSearchStatusUpdate_andCascadeDelete() = runBlocking {
        val downloadedThread = threadEntity(id = 2, content = "keyword thread", isDownloaded = false)
        val otherThread = threadEntity(id = 3, content = "other thread")
        val reply = replyEntity(id = 21, threadId = 2, content = "keyword reply")

        threadDao.insertThread(downloadedThread)
        threadDao.insertThread(otherThread)
        threadDao.insertReplies(listOf(reply))
        threadDao.updateDownloadStatus(2, true)

        assertEquals(listOf(downloadedThread.copy(isDownloaded = true)), threadDao.searchThreadsByContent("keyword"))
        assertEquals(listOf(reply), threadDao.searchRepliesByContent("keyword"))
        assertEquals(1, threadDao.getReplyCountByThreadId(2))
        assertTrue(threadDao.getAllThreads().first().contains(downloadedThread.copy(isDownloaded = true)))

        threadDao.deleteThread(downloadedThread.copy(isDownloaded = true))

        assertEquals(0, threadDao.getReplyCountByThreadId(2))
        assertEquals(null, threadDao.getSingleThread(2))
        assertEquals(null, threadDao.getSingleReply(21))
    }

    private fun threadEntity(
        id: Int,
        content: String,
        isDownloaded: Boolean = true
    ): ThreadEntity {
        return ThreadEntity(
            id = id,
            replyCount = 0,
            img = "img-$id",
            ext = ".png",
            now = "now-$id",
            userHash = "user-$id",
            name = "name-$id",
            title = "title-$id",
            content = content,
            isDownloaded = isDownloaded
        )
    }

    private fun replyEntity(
        id: Int,
        threadId: Int,
        content: String
    ): ReplyEntity {
        return ReplyEntity(
            id = id,
            threadId = threadId,
            img = "img-$id",
            ext = ".jpg",
            now = "now-$id",
            userHash = "user-$id",
            name = "name-$id",
            title = "title-$id",
            content = content
        )
    }
}
