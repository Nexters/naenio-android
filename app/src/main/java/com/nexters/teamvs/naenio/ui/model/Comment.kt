package com.nexters.teamvs.naenio.ui.model

open class BaseComment(
    open val id: Int,
    open val writer: User,
    open val content: String,
    open val likeCount: Long,
    open val isLiked: Boolean,
    open val writeTime: Long,
)

data class Comment(
    override val id: Int,
    override val writer: User = User.mock,
    val postId: Int = 0,
    val replyCount: Int = 0,
    override val content: String,
    override val isLiked: Boolean = false,
    override val likeCount: Long = 0,
    override val writeTime: Long,
): BaseComment(
    id = id,
    writer = writer,
    likeCount = likeCount,
    content = content,
    isLiked = isLiked,
    writeTime = writeTime
) {
    companion object {
        val mock = listOf<Comment>(
            Comment(
                id = 0,
                content = "No New Year's day to celebrate No chocolate covered candy hearts to give away No first of sprin",
                writeTime = 123123123,
                isLiked = true
            ),
            Comment(
                id = 1,
                content = "g No song to sing In fact it's just another ordinary day No April rain No flowers bloom No weddin",
                writeTime = 123123123,
            ),
            Comment(
                id = 2,
                content = "너를 보내고 눈부시게 아름답던 우리의 지난날이 그리워 밤이 새도록 니 사진을 보며 울다 지쳐 잠이 들곤해 나만",
                writeTime = 123123123,
                isLiked = true
            ),
            Comment(
                id = 3,
                content = "그대로 하고 싶은데 너의 얘기대로 사실 나 자신이 없어 너 없이 행복하란 말 아프지 말란 말 힘없이 끄덕였지만",
                writeTime = 123123123,
                isLiked = true
            ),
            Comment(
                id = 4,
                content = "다투기도 모진 말로 상처받기도 화해하고 또 마음에 남더라도 (나도 너도) 좋은 것만 기억해 나쁜 건",
                writeTime = 123123123,
            ),
        )
    }
}

data class Reply(
    override val id: Int,
    val parentId: Int = -1,
    override val content: String,
    override val writer: User = User.mock,
    override val likeCount: Long = 0,
    override val isLiked: Boolean = false,
    override val writeTime: Long,
): BaseComment(
    id = id,
    content = content,
    writer = writer,
    likeCount = likeCount,
    isLiked = isLiked,
    writeTime = writeTime
) {
    companion object {
        val mock = listOf<Reply>(
            Reply(
                id = 0,
                content = "그대로 하고 싶은데",
                writeTime = 123123123,
                isLiked = true
            ),
            Reply(
                id = 0,
                content = "너의 얘기대로",
                writeTime = 123123123,
            ),
            Reply(
                id = 0,
                content = "너 없이 행복하란 말",
                writeTime = 123123123,
                isLiked = true
            ),
        )
    }
}
