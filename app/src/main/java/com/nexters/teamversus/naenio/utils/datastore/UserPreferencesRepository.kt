package com.nexters.teamversus.naenio.utils.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
    }

    private val Context.userDataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    private object PreferencesKeys {
        val KEY_USER_ID = intPreferencesKey("KEY_USER_ID")
        val KEY_NICKNAME = stringPreferencesKey("KEY_NICKNAME")
        val KEY_PROFILE_IMAGE_INDEX = intPreferencesKey("KEY_PROFILE_IMAGE_INDEX")
        val KEY_AUTH_SERVICE_TYPE = stringPreferencesKey("KEY_AUTH_SERVICE_TYPE")
    }

    val userPrefFlow: Flow<UserPref?> = context.userDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // Get our show completed value, defaulting to false if not set:
            val userId = preferences[PreferencesKeys.KEY_USER_ID]
            val nickname = preferences[PreferencesKeys.KEY_NICKNAME]
            val profileImageIndex = preferences[PreferencesKeys.KEY_PROFILE_IMAGE_INDEX]
            val authServiceType = preferences[PreferencesKeys.KEY_AUTH_SERVICE_TYPE]
            UserPref(
                id = userId ?: return@map null,
                profileImageIndex = profileImageIndex ?: 0,
                nickname = nickname,
                authServiceType = authServiceType ?: return@map null
            )
        }

    fun getSyncUserPref(): UserPref {
        var userPref: UserPref? = null
        runBlocking {
            context.userDataStore.data.first { preferences ->
                val userId = preferences[PreferencesKeys.KEY_USER_ID]
                val nickname = preferences[PreferencesKeys.KEY_NICKNAME]
                val profileImageIndex = preferences[PreferencesKeys.KEY_PROFILE_IMAGE_INDEX]
                val authServiceType = preferences[PreferencesKeys.KEY_AUTH_SERVICE_TYPE]
                userPref = UserPref(
                    id = userId!!,
                    profileImageIndex = profileImageIndex ?: 0,
                    nickname = nickname,
                    authServiceType = authServiceType!!
                )
                true
            }
        }
        return userPref!!
    }

    suspend fun updateUserPreferences(user: UserPref) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_USER_ID] = user.id
            user.nickname?.let {
                preferences[PreferencesKeys.KEY_NICKNAME] = it
            }
            preferences[PreferencesKeys.KEY_PROFILE_IMAGE_INDEX] = user.profileImageIndex
            preferences[PreferencesKeys.KEY_AUTH_SERVICE_TYPE] = user.authServiceType
        }
    }

    suspend fun saveNickname(nickname: String) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_NICKNAME] = nickname
        }
    }

    suspend fun saveProfileImage(profileImageIndex: Int) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_PROFILE_IMAGE_INDEX] = profileImageIndex
        }
    }

    suspend fun clear() {
        context.userDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}