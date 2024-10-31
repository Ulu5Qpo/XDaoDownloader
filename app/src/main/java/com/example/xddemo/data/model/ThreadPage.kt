package com.example.xddemo.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ThreadPage(
    @PrimaryKey val id: Int,
    @SerializedName(value = "ReplyCount") val replyCount: Int,
    val img: String,
    val ext: String,
    val now: String,
    @SerializedName(value = "user_hash") val userHash: String,
    val name: String,
    val title: String,
    val content: String,
    @SerializedName(value = "Replies") val replies: List<Reply>
)

fun ThreadPage.toThreadEntity(): ThreadEntity {
    return ThreadEntity(
        id = this.id,
        replyCount = this.replyCount,
        img = this.img,
        ext = this.ext,
        now = this.now,
        userHash = this.userHash,
        name = this.name,
        title = this.title,
        content = this.content,
    )
}