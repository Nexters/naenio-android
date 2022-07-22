package com.nexters.teamversus.naenio.utils.datastore

object AuthDataStore {
    private const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"

    var authToken: String
        get() = DataStoreUtils.getSyncData(KEY_AUTH_TOKEN, "")
        set(value) = DataStoreUtils.saveSyncStringData(KEY_AUTH_TOKEN, value = value)
}