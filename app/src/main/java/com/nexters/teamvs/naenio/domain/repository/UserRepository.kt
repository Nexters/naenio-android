package com.nexters.teamvs.naenio.domain.repository

import android.util.Log
import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.UserApi
import com.nexters.teamvs.naenio.data.network.dto.*
import com.nexters.teamvs.naenio.domain.mapper.ProfileMapper.toMyProfile
import com.nexters.teamvs.naenio.domain.model.Profile
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val authDataStore: AuthDataStore = AuthDataStore
): BaseRepository() {

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

    suspend fun isExistNickname(nickname: String): IsExistNicknameResponse {
        return userApi.isExistNickname(nickname)
    }

    suspend fun setNickname(nickname: String): Boolean {
        val response = userApi.setNickname(NicknameRequest(nickname))
        return response.nickname == nickname
    }

    suspend fun getMyProfile() : Profile {
        return userApi.getMyProfile().toMyProfile()
    }

    suspend fun deleteProfile() : Boolean {
        return userApi.deleteProfile() == {}
    }
}