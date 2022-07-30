package com.nexters.teamvs.naenio.data

import android.util.Log
import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.NaenioApi
import com.nexters.teamvs.naenio.data.network.dto.AuthType
import com.nexters.teamvs.naenio.data.network.dto.IsExistNicknameResponse
import com.nexters.teamvs.naenio.data.network.dto.LoginRequest
import com.nexters.teamvs.naenio.data.network.dto.NicknameRequest
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val naenioApi: NaenioApi,
    private val authDataStore: AuthDataStore = AuthDataStore
): BaseRepository() {

    suspend fun login(oAuthToken: String, authType: AuthType): String {
        val jwt = naenioApi.login(LoginRequest(oAuthToken, authType)).token
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
        return naenioApi.isExistNickname(nickname)
    }

    suspend fun setNickname(nickname: String): Boolean {
        val response = naenioApi.setNickname(NicknameRequest(nickname))
        return response.nickname == nickname
    }
}