package com.nexters.teamvs.naenio.ui.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.data.network.dto.CommentParentType
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.model.UiState
import kotlinx.coroutines.launch

@Composable
fun ReplySheetLayout(
    modifier: Modifier,
    commentViewModel: CommentViewModel,
    parentComment: Comment,
    changeMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    val replies = commentViewModel.replies.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val inputUiState by remember { commentViewModel.inputUiState }
    val commentUiState by remember { commentViewModel.commentUiState }

    LaunchedEffect(key1 = parentComment.id, block = {
        commentViewModel.loadFirstReplies(parentComment.id)
    })

    val eventListener: (CommentEvent) -> Unit = {
        when (it) {
            is CommentEvent.Write -> {
                commentViewModel.writeComment(
                    postId = it.parentId,
                    content = it.content,
                )
            }
            CommentEvent.Close -> {

            }
            is CommentEvent.Like -> {
                if (it.comment.isLiked) commentViewModel.unlike(id = it.comment.id)
                else commentViewModel.like(id = it.comment.id)
            }
            CommentEvent.More -> {

            }
        }
    }

    Column(
        modifier = modifier
    ) {
        ReplyHeader(changeMode = changeMode, onEvent = onEvent)
        ReplyList(
            modifier = Modifier.weight(1f),
            listState = listState,
            parentComment = parentComment,
            uiState = commentUiState,
            comments = replies.value,
            mode = CommentMode.REPLY(parentComment),
            changeMode = changeMode,
            onLoadMore = {
                commentViewModel.loadMoreReplies(parentComment.id, it)
            },
            onEvent = eventListener
        )
        ReplyInput(
            scrollToTop = {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            uiState = inputUiState,
            commentId = parentComment.id,
            onEvent = {
                when (it) {
                    is CommentEvent.Write -> {
                        commentViewModel.writeReply(
                            commentId = it.parentId,
                            content = it.content
                        )
                    }
                    else -> {

                    }
                }
            }
        )
    }
}

@Composable
fun ReplyHeader(
    changeMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.clickable {
                changeMode.invoke(CommentMode.COMMENT)
            },
            painter = painterResource(id = R.drawable.ic_back_left),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(start = 5.dp, end = 9.dp),
            text = stringResource(id = R.string.reply_comment),
            color = Color.White,
            style = Font.pretendardSemiBold16
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.clickable {
                onEvent.invoke(CommentEvent.Close)
            },
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "close"
        )
    }
}

@Composable
fun ReplyList(
    modifier: Modifier,
    listState: LazyListState,
    parentComment: Comment,
    mode: CommentMode,
    uiState: UiState,
    comments: List<BaseComment>,
    onLoadMore: (Int) -> Unit,
    changeMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    val nextKey = comments.lastOrNull()?.id
    val requestLoadMoreKey = comments.getOrNull(comments.size - 1)?.id //TODO 사이즈 조용

    LazyColumn(modifier = modifier, state = listState) {
        item {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 19.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyColors.grey3f3f3f)
            )
        }
        item {
            CommentItem(
                comment = parentComment,
                mode = mode,
                onCommentMode = changeMode,
                onEvent = onEvent
            )
        }
        items(comments) {
            if (requestLoadMoreKey == it.id && nextKey != null && comments.size > 10) {
                onLoadMore.invoke(nextKey)
            }
            CommentItem(
                comment = it,
                mode = mode,
                onCommentMode = changeMode,
                onEvent = onEvent
            )
        }
        item {
            when (uiState) {
                UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MyColors.grey_d9d9d9)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ReplyInput(
    scrollToTop: () -> Unit,
    uiState: UiState,
    commentId: Int,
    onEvent: (CommentEvent) -> Unit
) {
    CommentEditText(
        scrollToTop = scrollToTop,
        uiState = uiState
    ) {
        onEvent.invoke(
            CommentEvent.Write(
                parentId = commentId,
                parentType = CommentParentType.COMMENT,
                content = it
            )
        )
    }
}