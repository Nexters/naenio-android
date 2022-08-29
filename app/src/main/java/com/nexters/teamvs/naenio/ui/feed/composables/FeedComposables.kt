package com.nexters.teamvs.naenio.ui.feed.composables

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.tabs.auth.model.Profile

@Composable
fun VoteContent(post: Post, modifier: Modifier, maxLine: Int) {
    Row(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_gift), contentDescription = "ic_gift"
        )
        Text(
            text = "${post.totalVoteCount}명 투표",
            color = Color.White,
            style = Font.body2,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    Text(
        text = post.title,
        color = Color.White,
        style = Font.pretendardSemiBold20,
        modifier = Modifier.padding(top = 8.dp),
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 29.sp
    )
    Text(
        text = post.content,
        color = Color.White,
        style = Font.pretendardRegular14,
        modifier = Modifier.padding(top = 10.dp, bottom = 8.dp),
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CommentLayout(commentCount: Int = 0, modifier: Modifier) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_comment_icon),
            contentDescription = "ic_comment_icon"
        )
        Text(
            text = "댓글",
            color = Color.White,
            style = Font.pretendardSemiBold16,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = "${commentCount}개",
            color = Color.White,
            style = Font.pretendardRegular16,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
fun ProfileNickName(
    modifier: Modifier,
    isIconVisible: Boolean,
    isVisibleShareIcon: Boolean = true,
    nickName: String = "",
    profileImageIndex: Int = 0,
    onShare: () -> Unit,
    onMore: () -> Unit,
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImageIcon(index = profileImageIndex, size = 24.dp)
        Text(
            text = nickName,
            color = Color.White,
            style = Font.pretendardMedium16,
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isIconVisible) {

            if (isVisibleShareIcon) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onShare.invoke()
                        },
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "icon_feed_more"
                )
            }

            Image(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        onMore.invoke()
                    },
                painter = painterResource(R.drawable.ic_feed_more),
                contentDescription = "icon_feed_more"
            )
        }
    }
}

@Composable
fun ProfileImageIcon(index: Int = 0, size: Dp, padding: Dp = 0.dp) {
    Image(
        painter = painterResource(id = Profile.images[index].image),
        contentDescription = "profile-img",
        modifier = Modifier
            .size(size)
            .background(MyColors.mint_83d8c8, shape = CircleShape)
            .padding(padding)
    )
}

@Composable
fun TopBar(
    modifier: Modifier,
    barTitle: String?,
    close: () -> Unit,
    isMoreBtnVisible: Boolean = true,
    textStyle: TextStyle = Font.pretendardSemiBold16,
    post: Post? = null,
) {

    val context = LocalContext.current
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.clickable {
                close.invoke()
            },
            painter = painterResource(R.drawable.ic_back_left),
            contentDescription = "icon_back_m",
        )
        Spacer(modifier = Modifier.weight(1f))
        barTitle?.let { barTitle ->
            if (barTitle != "") {
                Text(
                    text = barTitle, style = textStyle, color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        val iconVisible = if (isMoreBtnVisible) 1f else 0f
        Image(modifier = Modifier
            .clickable {
                //share
                if (post == null) return@clickable
                val shareLink = "https://naenio.shop/posts/${post.id}"

                val type = "text/plain"
                val subject = "네니오로 오세요~~~"
                val extraText = shareLink
                val shareWith = "ShareWith"

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)

                ContextCompat.startActivity(
                    context,
                    Intent.createChooser(intent, shareWith),
                    null
                )
            }
            .alpha(iconVisible),
            painter = painterResource(R.drawable.ic_feed_more),
            contentDescription = "icon_feed_more")
    }
}

@Composable
fun contentEmptyLayout(@StringRes stringId: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_empty), contentDescription = "icon_empty"
        )
        Text(
            modifier = Modifier.padding(top = 14.dp),
            text = stringResource(id = stringId),
            style = Font.pretendardMedium18,
            color = MyColors.darkGrey_828282
        )
    }
}
