package com.nexters.teamvs.naenio.ui.profile

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.domain.model.*
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
) : BaseViewModel() {

    private val _myProfile = MutableStateFlow<User?>(null)
    val myProfile = _myProfile.asStateFlow()

    private val _myCommentList = MutableStateFlow<List<MyComments>?>(null)
    val myCommentList = _myCommentList.asStateFlow()

    private val _noticeList = MutableStateFlow<List<Notice>?>(null)
    val noticeList = _noticeList.asStateFlow()

    private val _notice = MutableStateFlow<Notice?>(null)
    val notice = _notice.asStateFlow()


    init {
        getMyProfile()
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
                _myProfile.value = userRepository.getMyProfile(viewModelScope)
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

    fun logout() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                userRepository.logOut()
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                userRepository.signOut()
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
                GlobalUiEvent.uiEvent.tryEmit(UiEvent.HideDialog)
            }
        }
    }
}