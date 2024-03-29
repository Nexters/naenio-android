package com.nexters.teamversus.naenio.ui.comment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nexters.teamversus.naenio.R
import com.nexters.teamversus.naenio.base.GlobalUiEvent
import com.nexters.teamversus.naenio.data.network.dto.CommentParentType
import com.nexters.teamversus.naenio.theme.Font
import com.nexters.teamversus.naenio.theme.MyColors
import com.nexters.teamversus.naenio.ui.component.MenuDialogModel
import com.nexters.teamversus.naenio.ui.model.UiState
import kotlinx.coroutines.launch

@Composable
fun ReplyScreenContent(
    modifier: Modifier,
    replyViewModel: ReplyViewModel,
    mode: CommentMode,
    parentComment: Comment,
    changeMode: (CommentMode) -> Unit,
    onClose: () -> Unit,
) {
    val replies = replyViewModel.replies.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val inputUiState by remember { replyViewModel.inputUiState }
    val commentUiState by remember { replyViewModel.commentUiState }
    val isRefreshing = replyViewModel.isRefreshing.collectAsState()
    val user = replyViewModel.user.collectAsState(initial = null)
    val selectedComment = replyViewModel.selectedComment.collectAsState()

    BackHandler {
        if (mode is CommentMode.REPLY) {
            replyViewModel.clear()
            changeMode.invoke(CommentMode.COMMENT)
        }
    }

    LaunchedEffect(key1 = parentComment.id, block = {
        replyViewModel.loadNextPage(parentComment.id)
        replyViewModel.setSelectedComment(parentComment)
    })

    val eventListener: (CommentEvent) -> Unit = {
        when (it) {
            is CommentEvent.Write -> {
                replyViewModel.writeReply(
                    commentId = it.parentId,
                    content = it.content,
                )
            }
            CommentEvent.Close -> {
                onClose.invoke()
            }
            is CommentEvent.Like -> {
                if (it.comment is Comment) {
                    if (it.comment.isLiked) replyViewModel.unlikeComment(id = it.comment.id)
                    else replyViewModel.likeComment(id = it.comment.id)
                } else {
                    if (it.comment.isLiked) replyViewModel.unlike(id = it.comment.id)
                    else replyViewModel.like(id = it.comment.id)
                }
            }
            is CommentEvent.More -> {
                if (user.value?.id == it.comment.writer.id) {
                    scope.launch {
                        GlobalUiEvent.showMenuDialog(
                            listOf(
                                MenuDialogModel(
                                    text = "삭제하기",
                                    color = Color.Red,
                                    onClick = {
                                        if (it.comment is Comment) {
                                            replyViewModel.deleteComment(it.comment)
                                        } else {
                                            replyViewModel.delete(it.comment as Reply)
                                        }
                                    }
                                )
                            )
                        )
                    }
                } else {
                    scope.launch {
                        GlobalUiEvent.showMenuDialog(
                            listOf(
                                MenuDialogModel(
                                    text = "사용자 차단하기",
                                    color = Color.Red,
                                    onClick = {
                                        replyViewModel.report(it.comment.writer.id)
                                    }
                                ),
                                MenuDialogModel(
                                    text = "댓글 신고하기",
                                    color = Color.Red,
                                    onClick = {
                                        replyViewModel.report(it.comment.writer.id)
                                    }
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        ReplyHeader(changeMode = changeMode, onEvent = eventListener)
        ReplyList(
            modifier = Modifier.weight(1f),
            listState = listState,
            parentComment = selectedComment.value,
            uiState = commentUiState,
            comments = replies.value,
            mode = CommentMode.REPLY(parentComment),
            changeMode = changeMode,
            onLoadMore = {
                replyViewModel.loadNextPage(parentComment.id)
            },
            isRefreshing = isRefreshing.value,
            onRefresh = {
                replyViewModel.refresh(parentComment.id)
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
                        replyViewModel.writeReply(
                            commentId = it.parentId,
                            content = it.content
                        )
                    }
                    else -> {

                    }
                }
            },
            profileImageIndex = user.value?.profileImageIndex ?: 0
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
    parentComment: Comment?,
    mode: CommentMode,
    uiState: UiState,
    comments: List<BaseComment>,
    onLoadMore: (Int) -> Unit,
    changeMode: (CommentMode) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    if (parentComment == null) return
    val nextKey = comments.lastOrNull()?.id
    val requestLoadMoreKey = comments.getOrNull(comments.size - 1)?.id //TODO 사이즈 조용

    SwipeRefresh(
        modifier = modifier,
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                backgroundColor = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small,
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
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
}

@Composable
fun ReplyInput(
    scrollToTop: () -> Unit,
    uiState: UiState,
    commentId: Int,
    profileImageIndex: Int,
    onEvent: (CommentEvent) -> Unit
) {
    CommentEditText(
        scrollToTop = scrollToTop,
        uiState = uiState,
        profileImageIndex = profileImageIndex,
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