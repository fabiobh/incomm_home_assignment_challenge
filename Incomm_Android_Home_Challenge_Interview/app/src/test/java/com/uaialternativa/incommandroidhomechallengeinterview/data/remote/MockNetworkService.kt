package com.uaialternativa.incommandroidhomechallengeinterview.data.remote

import com.uaialternativa.incommandroidhomechallengeinterview.data.model.UserResponse
import retrofit2.http.GET

class MockNetworkService : ApiService {
    var response: UserResponse = UserResponse("ok", 0, emptyList())
    var shouldThrowError = false

    override suspend fun getUsers(): UserResponse {
        if (shouldThrowError) {
            throw Exception("Mock Network Error")
        }
        return response
    }
}
