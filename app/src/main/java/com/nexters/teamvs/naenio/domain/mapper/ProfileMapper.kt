package com.nexters.teamvs.naenio.domain.mapper

import com.nexters.teamvs.naenio.data.network.dto.MyProfileResponse
import com.nexters.teamvs.naenio.data.network.dto.NoticeListResponse
import com.nexters.teamvs.naenio.domain.model.Notice
import com.nexters.teamvs.naenio.domain.model.User
import com.nexters.teamvs.naenio.utils.datastore.UserPref

object ProfileMapper {

    fun UserPref.toUser(): User {
        return User(
            id = id,
            nickname = nickname,
            authServiceType = authServiceType,
            profileImageIndex = profileImageIndex,
        )
    }

    fun MyProfileResponse.toUserPref(): UserPref {
        return UserPref(
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