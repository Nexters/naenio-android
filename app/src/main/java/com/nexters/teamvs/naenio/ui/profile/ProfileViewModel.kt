package com.nexters.teamvs.naenio.ui.profile

import android.app.Activity
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.domain.model.MyComments
import com.nexters.teamvs.naenio.domain.model.Notice
import com.nexters.teamvs.naenio.domain.model.User
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
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

    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .build()

    init {
        getMyProfile()
    }

    fun setType(profileType: String) {
        if (profileType.contains(ProfileType.NOTICE_DETAIL)) {
            val noticeId = profileType.replace(ProfileType.NOTICE_DETAIL + "=", "").toInt()
            getNoticeList(noticeId)
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

    private fun getNoticeList(noticeId: Int? = null) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _noticeList.value = userRepository.getNoticeList()
                _notice.value = _noticeList.value?.filter { it.id == noticeId }?.getOrNull(0)
                Log.d("###getNoticeList", _notice.toString())
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

    fun logout(activity: Activity) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                userRepository.logOut()
                googleLogout(activity)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun signOut(activity: Activity) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                userRepository.signOut()
                googleRevokeAccess(activity)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
                GlobalUiEvent.uiEvent.tryEmit(UiEvent.HideDialog)
            }
        }
    }

    private fun googleLogout(activity: Activity) {
        val googleSignInClient = GoogleSignIn.getClient(activity, gso);
        googleSignInClient.signOut()
    }

    private fun googleRevokeAccess(activity: Activity) {
        val googleSignInClient = GoogleSignIn.getClient(activity, gso);
        googleSignInClient.revokeAccess()
    }
}