package com.nexters.teamversus.naenio.domain.repository

import android.util.Log
import com.nexters.teamversus.naenio.base.BaseRepository
import com.nexters.teamversus.naenio.data.network.api.UserApi
import com.nexters.teamversus.naenio.data.network.dto.AuthType
import com.nexters.teamversus.naenio.data.network.dto.BlockRequest
import com.nexters.teamversus.naenio.data.network.dto.LoginRequest
import com.nexters.teamversus.naenio.data.network.dto.NicknameRequest
import com.nexters.teamversus.naenio.data.network.dto.ProfileImageRequest
import com.nexters.teamversus.naenio.domain.mapper.ProfileMapper.toNoticeList
import com.nexters.teamversus.naenio.domain.mapper.ProfileMapper.toUser
import com.nexters.teamversus.naenio.domain.mapper.ProfileMapper.toUserPref
import com.nexters.teamversus.naenio.domain.model.Notice
import com.nexters.teamversus.naenio.domain.model.User
import com.nexters.teamversus.naenio.utils.datastore.AuthDataStore
import com.nexters.teamversus.naenio.utils.datastore.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authDataStore: AuthDataStore = AuthDataStore,
) : BaseRepository() {

    suspend fun login(oAuthToken: String, authType: AuthType): String {
        val jwt = userApi.login(LoginRequest(oAuthToken, authType)).token
        if (jwt.isNotEmpty()) {
            authDataStore.authToken = jwt
            Log.d(className, "$authType:: $oAuthToken")
            Log.d(className, "jwt:: $jwt")
        } else {
            throw IllegalStateException("Token is Empty.")
        }
        return jwt
    }

    suspend fun isExistNickname(nickname: String): Boolean {
        return userApi.isExistNickname(nickname).exist
    }

    //TODO 같은 값이면 요청 막기
    suspend fun setNickname(nickname: String) {
        userApi.setNickname(NicknameRequest(nickname)).also {
            userPreferencesRepository.saveNickname(it.nickname)
        }
    }

    suspend fun getMyProfile(externalScope: CoroutineScope): User {
        val userPrefValue = userPreferencesRepository.userPrefFlow.stateIn(externalScope).value
        return if (userPrefValue == null) {
            val profileResponse = userApi.getMyProfile()
            profileResponse.toUserPref().let {
                userPreferencesRepository.updateUserPreferences(it)
                it.toUser()
            }
        } else {
            userPrefValue.toUser()
        }
    }

    fun getUserFlow(): Flow<User> {
        return userPreferencesRepository.userPrefFlow.mapNotNull { it?.toUser() }
    }

    private suspend fun clearUserCache() {
        userPreferencesRepository.clear()
        authDataStore.authToken = ""
    }

    suspend fun logOut() {
        clearUserCache()
    }

    suspend fun signOut() {
        userApi.deleteProfile()
        clearUserCache()
    }

    //TODO 같은 값이면 요청 막기
    suspend fun setProfileImage(index: Int) {
        userApi.setProfileImage(ProfileImageRequest(index)).also {
            userPreferencesRepository.saveProfileImage(it.profileImageIndex)
        }
    }

    suspend fun getNoticeList(): List<Notice> {
        return userApi.getNotice().toNoticeList()
    }

    suspend fun block(userId: Int) {
        return userApi.block(BlockRequest(userId))
    }
}