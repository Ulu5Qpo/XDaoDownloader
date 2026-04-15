package com.example.xddemo

import com.example.xddemo.data.model.Reply
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity
import com.example.xddemo.data.model.ThreadPage
import com.example.xddemo.data.model.ThreadWithReplies

fun reply(
    id: Int,
    content: String = "reply-content-$id",
    userHash: String = "user-$id"
): Reply {
    return Reply(
        id = id,
        img = "img-$id",
        ext = ".jpg",
        now = "now-$id",
        userHash = userHash,
        name = "name-$id",
        title = "title-$id",
        content = content
    )
}

fun threadPage(
    id: Int,
    replyCount: Int = 0,
    replies: List<Reply> = emptyList(),
    content: String = "thread-content-$id",
    userHash: String = "thread-user-$id"
): ThreadPage {
    return ThreadPage(
        id = id,
        replyCount = replyCount,
        img = "img-$id",
        ext = ".png",
        now = "now-$id",
        userHash = userHash,
        name = "thread-name-$id",
        title = "thread-title-$id",
        content = content,
        replies = replies
    )
}

fun threadEntity(
    id: Int,
    content: String = "thread-content-$id",
    isDownloaded: Boolean = true,
    userHash: String = "thread-user-$id"
): ThreadEntity {
    return ThreadEntity(
        id = id,
        replyCount = 0,
        img = "img-$id",
        ext = ".png",
        now = "now-$id",
        userHash = userHash,
        name = "thread-name-$id",
        title = "thread-title-$id",
        content = content,
        isDownloaded = isDownloaded
    )
}

fun replyEntity(
    id: Int,
    threadId: Int = id,
    content: String = "reply-content-$id",
    userHash: String = "user-$id"
): ReplyEntity {
    return ReplyEntity(
        id = id,
        threadId = threadId,
        img = "img-$id",
        ext = ".jpg",
        now = "now-$id",
        userHash = userHash,
        name = "name-$id",
        title = "title-$id",
        content = content
    )
}

fun threadWithReplies(
    thread: ThreadEntity,
    replies: List<ReplyEntity>
): ThreadWithReplies {
    return ThreadWithReplies(thread = thread, replies = replies)
}
