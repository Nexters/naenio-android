package com.nexters.teamversus.naenio.data.network.api

import kotlinx.serialization.json.JsonElement
import retrofit2.http.GET

interface NaenioApi {
    @GET("posts/1")
    suspend fun getData(): JsonElement
}