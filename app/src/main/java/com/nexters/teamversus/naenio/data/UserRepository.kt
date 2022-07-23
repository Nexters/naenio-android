package com.nexters.teamversus.naenio.data

import com.nexters.teamversus.naenio.data.network.ApiProvider
import com.nexters.teamversus.naenio.data.network.api.NaenioApi
import com.nexters.teamversus.naenio.data.network.dto.AuthType
import com.nexters.teamversus.naenio.data.network.dto.IsExistNicknameResponse
import com.nexters.teamversus.naenio.data.network.dto.LoginRequest
import com.nexters.teamversus.naenio.data.network.dto.NicknameRequest

class UserRepository(
    private val naenioApi: NaenioApi = ApiProvider.retrofit.create(NaenioApi::class.java)
) {

    suspend fun login(token: String, authType: AuthType): String {
        val response = naenioApi.login(LoginRequest(token, authType))
        return response.token
    }

    suspend fun isExistNickname(nickname: String): IsExistNicknameResponse {
        return naenioApi.isExistNickname(nickname)
    }

    suspend fun setNickname(nickname: String): Boolean {
        val response = naenioApi.setNickname(NicknameRequest(nickname))
        return response.nickname == nickname
    }
}