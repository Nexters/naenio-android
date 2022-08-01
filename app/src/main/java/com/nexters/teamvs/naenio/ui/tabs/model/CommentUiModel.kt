package com.nexters.teamvs.naenio.ui.tabs.model

data class CommentUiModel(
    val id: Int,
    val userId: Int,
    val parentId: Int? = null,
    val parentType: CommentParentType = CommentParentType.POST,
    val content: String,
    val createdTime: Long,
    val modifiedTime: Long? = null,
)

enum class CommentParentType {
    POST, COMMENT
}