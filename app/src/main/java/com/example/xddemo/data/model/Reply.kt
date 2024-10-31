package com.example.xddemo.data.model

import com.google.gson.annotations.SerializedName

data class Reply(
    val id: Int,
    val img: String,
    val ext: String,
    val now: String,
    @SerializedName(value = "user_hash") val userHash: String,
    val name: String,
    val title: String,
    val content: String
)

fun Reply.toReplyEntity(): ReplyEntity {
    return ReplyEntity(
        id = this.id,
        img = this.img,
        ext = this.ext,
        now = this.now,
        userHash = this.userHash,
        name = this.name,
        title = this.title,
        content = this.content,
        threadId = 0
    )
}




