package com.nexters.teamvs.naenio.domain.repository

import android.util.Log
import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.UserApi
import com.nexters.teamvs.naenio.data.network.dto.AuthType
import com.nexters.teamvs.naenio.data.network.dto.LoginRequest
import com.nexters.teamvs.naenio.data.network.dto.NicknameRequest
import com.nexters.teamvs.naenio.data.network.dto.ProfileImageRequest
import com.nexters.teamvs.naenio.domain.mapper.ProfileMapper.toNoticeList
import com.nexters.teamvs.naenio.domain.mapper.ProfileMapper.toProfile
import com.nexters.teamvs.naenio.domain.mapper.ProfileMapper.toUserPref
import com.nexters.teamvs.naenio.domain.model.Notice
import com.nexters.teamvs.naenio.domain.model.Profile
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import com.nexters.teamvs.naenio.utils.datastore.UserPreferencesRepository
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

    suspend fun setNickname(nickname: String): Boolean {
        val response = userApi.setNickname(NicknameRequest(nickname))
        return response.nickname == nickname
    }

    suspend fun getMyProfile(): Profile {
        val profileResponse = userApi.getMyProfile()
        val userPref = profileResponse.toUserPref()
        val profile = userPref.toProfile()
        userPreferencesRepository.updateUserPreferences(
            userPref
        )
        return profile
    }

    suspend fun deleteProfile() {
        userApi.deleteProfile()
    }

    suspend fun setProfileImage(index: Int): Boolean {
        val response = userApi.setProfileImage(ProfileImageRequest(index))
        return index == response.profileImageIndex
    }

    suspend fun getNoticeList(): List<Notice> {
        return userApi.getNotice().toNoticeList()
    }
}