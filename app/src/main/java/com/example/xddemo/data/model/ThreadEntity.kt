package com.example.xddemo.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "threads",
    indices = [Index(value = ["content"])]
)
data class ThreadEntity(
    @PrimaryKey val id: Int,
    val replyCount: Int,
    val img: String,
    val ext: String,
    val now: String,
    val userHash: String,
    val name: String,
    val title: String,
    val content: String,
    val isDownloaded: Boolean = false
)

fun ThreadEntity.toReplyEntity(): ReplyEntity {
    return ReplyEntity(
        id = this.id,
        img = this.img,
        ext = this.ext,
        now = this.now,
        userHash = this.userHash,
        name = this.name,
        title = this.title,
        content = this.content,
        threadId = this.id
    )
}

@Entity(
    tableName = "replies",
    indices = [
        Index(value = ["threadId"]), // 为 threadId 列添加索引
        Index(value = ["content"])   // 为 content 列添加索引
    ],
    foreignKeys = [ForeignKey(
        entity = ThreadEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("threadId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReplyEntity(
    @PrimaryKey val id: Int,
    val threadId: Int,      // 外键关联到 ThreadEntity
    val img: String,
    val ext: String,
    val now: String,
    val userHash: String,
    val name: String,
    val title: String,
    val content: String
)

data class ThreadWithReplies(
    @Embedded val thread: ThreadEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "threadId"
    )
    val replies: List<ReplyEntity>
)