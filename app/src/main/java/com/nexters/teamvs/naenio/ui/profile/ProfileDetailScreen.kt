package com.nexters.teamvs.naenio.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.BuildConfig
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.model.Notice
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.comment.Comment
import com.nexters.teamvs.naenio.ui.component.MenuDialogModel
import com.nexters.teamvs.naenio.ui.feed.composables.ProfileNickName
import com.nexters.teamvs.naenio.ui.feed.composables.TopBar
import com.nexters.teamvs.naenio.ui.feed.composables.contentEmptyLayout
import kotlinx.coroutines.launch

@Composable
fun ProfileDetailScreen(
    profileType: String = "",
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit,
        block = {
            viewModel.setType(profileType)
        })

    var title = ""
    when (profileType) {
        ProfileType.MY_COMMENT -> title = "작성한 댓글"
        ProfileType.DEVELOPER -> title = "개발자 정보"
        ProfileType.VERSION -> title = "버전 정보"
        ProfileType.NOTICE -> title = "공지사항"
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
    ) {
        TopBar(
            modifier = Modifier,
            barTitle = title,
            close = {
                navController.popBackStack()
            },
            isMoreBtnVisible = false
        )
        if (profileType.contains(ProfileType.NOTICE_DETAIL)) {
            NoticeDetailLayout(viewModel = viewModel)
        }
        when (profileType) {
            ProfileType.MY_COMMENT -> {
                MyCommentLayout(navController= navController, viewModel = viewModel)
            }
            ProfileType.DEVELOPER -> {
                DeveloperLayout()
            }
            ProfileType.VERSION -> {
                VersionLayout()
            }
            ProfileType.NOTICE -> {
                NoticeLayout(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun NoticeDetailLayout(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val noticeState = viewModel.notice.collectAsState()
    val notice = noticeState.value
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = notice?.title.orEmpty(),
                style = Font.pretendardMedium20,
                color = Color.White,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
        item {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = notice?.content.orEmpty(),
                style = Font.pretendardMedium16,
                color = MyColors.grey_b3b3b3,
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun NoticeLayout(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val noticeListState = viewModel.noticeList.collectAsState()
    val noticeList = noticeListState.value
    val isNoticeListEmpty = noticeList == null || noticeList.isEmpty()
    Spacer(modifier = Modifier.height(20.dp))
    if (isNoticeListEmpty) {
        contentEmptyLayout(stringId = R.string.notice_empty)
    } else {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(noticeList!!) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MyColors.postBackgroundColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                        .clickable {
                            moveProfileDetailScreen(
                                navController = navController,
                                type = ProfileType.NOTICE_DETAIL + "=${item.id}"
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.title,
                        style = Font.pretendardMedium16,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.icon_back_s),
                        contentDescription = "icon_back_s"
                    )
                }
            }
        }
    }
}

@Composable
fun MyCommentLayout(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val myCommentListState = viewModel.myCommentList.collectAsState()
    val myCommentList = myCommentListState.value
    val isMyCommentEmpty = myCommentList == null || myCommentList.isEmpty()
    val scope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(20.dp))
    if (isMyCommentEmpty) {
        contentEmptyLayout(R.string.my_comment_empty)
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(myCommentList!!) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 20.dp)
                        .background(MyColors.postBackgroundColor, shape = RoundedCornerShape(16.dp))
                        .clickable {
                            navController.navigate("FeedCommentDetail/${it.post.id}")
                        }
                ) {
                    ProfileNickName(
                        nickName = it.post.author.nickname,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .padding(horizontal = 20.dp),
                        isIconVisible = true,
                        onMore = {
                            scope.launch {
                                GlobalUiEvent.showMenuDialog(
                                    MenuDialogModel(
                                        text = "삭제",
                                        color = Color.Red,
                                        onClick = {
                                            viewModel.deleteMyComment(it.id)
                                        }
                                    )
                                )
                            }
                        },
                        isVisibleShareIcon = false,
                        onShare = {}
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 14.dp)
                                .padding(horizontal = 20.dp),
                            text = it.post.title,
                            style = Font.pretendardMedium16,
                            color = Color.White,
                            lineHeight = 24.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                                Text (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MyColors.darkGrey_1e222c,
                                        shape = RoundedCornerShape(
                                            bottomStart = 16.dp,
                                            bottomEnd = 16.dp
                                        )
                                    )
                                    .padding(horizontal = 20.dp, vertical = 20.dp),
                        text = it.content,
                        color = Color.White,
                        style = Font.pretendardMedium16,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun VersionLayout() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "app_icon"
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "현재 버전",
            color = Color.White,
            style = Font.pretendardSemiBold16
        )
        Text(
            text = BuildConfig.VERSION_NAME,
            color = MyColors.grey_b3b3b3,
            style = Font.pretendardSemiBold16,
            lineHeight = 32.sp
        )
    }
}

@Composable
fun DeveloperLayout() {
    Spacer(modifier = Modifier.height(40.dp))
    LazyColumn {
        items(DeveloperItem.developerList) { developerList ->
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = developerList.title,
                    color = MyColors.darkGrey_828282,
                    style = Font.montserratMedium16
                )
                Text(
                    modifier = Modifier.padding(top = 6.dp, bottom = 26.dp),
                    text = developerList.content,
                    color = Color.White,
                    style = Font.pretendardMedium18
                )
            }
        }
    }
}

@Composable
@Preview
fun ProfileDetail() {
    ProfileDetailScreen(
        navController = NavHostController(LocalContext.current),
        profileType = ProfileType.NOTICE_DETAIL + "/4"
    )
}