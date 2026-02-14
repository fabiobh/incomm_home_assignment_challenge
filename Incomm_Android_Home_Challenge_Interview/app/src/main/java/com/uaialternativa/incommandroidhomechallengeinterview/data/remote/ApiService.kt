package com.uaialternativa.incommandroidhomechallengeinterview.data.remote

import com.uaialternativa.incommandroidhomechallengeinterview.data.model.UserResponse
import retrofit2.http.GET

/**
 * DATA LAYER
 *
 * Retrofit interface defining API endpoints.
 * Responsible for direct communication with the remote data source (Network).
 */
interface ApiService {
    @GET("incomm")
    suspend fun getUsers(): UserResponse
}
