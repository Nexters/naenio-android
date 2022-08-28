package com.nexters.teamvs.naenio.ui.profile

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.data.network.dto.MyProfileResponse
import com.nexters.teamvs.naenio.domain.model.*
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.home.ThemeItem
import com.nexters.teamvs.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
) : BaseViewModel() {

    private val _myProfile = MutableStateFlow<Profile?>(null)
    val myProfile = _myProfile.asStateFlow()

    private val _myCommentList = MutableStateFlow<List<MyComments>?>(null)
    val myCommentList = _myCommentList.asStateFlow()

    private val _noticeList = MutableStateFlow<List<Notice>?>(null)
    val noticeList = _noticeList.asStateFlow()

    private val _notice = MutableStateFlow<Notice?>(null)
    val notice = _notice.asStateFlow()


    init {
    }

    fun setType(profileType: String) {
        if (profileType.contains(ProfileType.NOTICE_DETAIL)) {
            val noticeId = profileType.replace(ProfileType.NOTICE_DETAIL + "=", "").toInt()
            getNotice(noticeId)
        }
        when (profileType) {
            ProfileType.MY_COMMENT -> {
                getMyCommentList()
            }
            ProfileType.NOTICE -> {
                getNoticeList()
            }
        }
    }

    private fun getNotice(noticeId: Int) {
        _notice.value = _noticeList.value?.filter { it.id == noticeId }?.getOrNull(0)
    }

    private fun getNoticeList() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _noticeList.value = userRepository.getNoticeList()
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    private fun getMyCommentList() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _myCommentList.value = commentRepository.getMyCommentList(10)
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun getMyProfile() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _myProfile.value = userRepository.getMyProfile()
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun deleteMyComment(commentId : Int) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                commentRepository.deleteComment(commentId)
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                getMyCommentList()
            }
        }
    }
}