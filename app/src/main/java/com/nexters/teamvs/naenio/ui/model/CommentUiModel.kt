package com.nexters.teamvs.naenio.ui.model

//TODO 댓글 모델과 대댓글 모댈을 나누는 방법으로 바꾸는 것도 고민
data class CommentUiModel(
    val id: Int,
    val userId: Int,
    val parentId: Int? = null,
    val parentType: CommentParentType = CommentParentType.POST,
    val childCommentCount: Int? = null,
    val content: String,
    val like: Boolean = false,
    val likeCount: Long = 0,
    private val createdTime: Long,
    private val modifiedTime: Long? = null,
) {
    val time get() = modifiedTime ?: createdTime

    companion object {
        val mock = listOf<CommentUiModel>(
            CommentUiModel(
                id = 0,
                userId = 0,
                content = "댓글댓글댓글댓글댓글",
                createdTime = 123123123,
            ),
            CommentUiModel(
                id = 0,
                userId = 0,
                content = "댓글댓글댓글댓글댓글",
                createdTime = 123123123,
            ),
            CommentUiModel(
                id = 0,
                userId = 0,
                content = "댓글댓글댓글댓글댓글",
                createdTime = 123123123,
            ),
            CommentUiModel(
                id = 0,
                userId = 0,
                content = "댓글댓글댓글댓글댓글",
                createdTime = 123123123,
            ),
            CommentUiModel(
                id = 0,
                userId = 0,
                content = "댓글댓글댓글댓글댓글",
                createdTime = 123123123,
            ),
            CommentUiModel(
                id = 0,
                userId = 0,
                content = "댓글댓글댓글댓글댓글",
                createdTime = 123123123,
            ),
        )
    }
}

enum class CommentParentType {
    POST, COMMENT
}