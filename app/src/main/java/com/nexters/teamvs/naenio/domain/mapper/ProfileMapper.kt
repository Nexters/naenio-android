package com.nexters.teamvs.naenio.domain.mapper

import com.nexters.teamvs.naenio.data.network.dto.MyProfileResponse
import com.nexters.teamvs.naenio.domain.model.Profile

object ProfileMapper {

    fun MyProfileResponse.toMyProfile(): Profile {
        return Profile(
            id = id,
            nickname = nickname,
            authServiceType = authServiceType,
            profileImageIndex = profileImageIndex,
        )
    }

}