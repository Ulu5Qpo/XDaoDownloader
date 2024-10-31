package com.example.xddemo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity
import com.example.xddemo.data.model.ThreadWithReplies
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreadDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertThread(thread: ThreadEntity)

    @Update
    suspend fun updateThread(thread: ThreadEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReplies(replies: List<ReplyEntity>)

    @Transaction
    @Query("SELECT * FROM threads WHERE id = :threadId")
    fun getThreadWithReplies(threadId: Int): Flow<ThreadWithReplies>

    @Transaction
    @Query("SELECT * FROM threads")
    fun getAllThreads(): Flow<List<ThreadEntity>>

    @Query("SELECT COUNT(*) FROM replies WHERE threadId = :id")
    suspend fun getReplyCountByThreadId(id: Int): Int

    @Query("SELECT * FROM replies WHERE id = :replyId")
    suspend fun getSingleReply(replyId: Int): ReplyEntity?

    @Query("SELECT * FROM threads WHERE id = :replyId")
    suspend fun getSingleThread(replyId: Int): ThreadEntity?

    @Query("SELECT * FROM replies WHERE content LIKE '%' || :keywords || '%'")
    suspend fun searchRepliesByContent(keywords: String): List<ReplyEntity>

    @Query("SELECT * FROM threads WHERE content LIKE '%' || :keywords || '%'")
    suspend fun searchThreadsByContent(keywords: String): List<ThreadEntity>

    @Query("UPDATE threads SET isDownloaded = :isDownloaded WHERE id = :threadId")
    suspend fun updateDownloadStatus(threadId: Int, isDownloaded: Boolean)

    @Delete
    suspend fun deleteThread(thread: ThreadEntity)

}