package com.nexters.teamvs.naenio.domain.mapper

import com.nexters.teamvs.naenio.data.network.dto.MyProfileResponse
import com.nexters.teamvs.naenio.data.network.dto.NoticeListResponse
import com.nexters.teamvs.naenio.domain.model.Notice
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

    fun NoticeListResponse.toNoticeList() : List<Notice> {
        return notices.map {
            Notice(
                id = it.id,
                title = it.title,
                content = it.content
            )
        }
    }
}