package com.uaialternativa.incommandroidhomechallengeinterview.data.repository

import com.uaialternativa.incommandroidhomechallengeinterview.data.model.toDomain
import com.uaialternativa.incommandroidhomechallengeinterview.data.remote.ApiService
import com.uaialternativa.incommandroidhomechallengeinterview.domain.model.User
import com.uaialternativa.incommandroidhomechallengeinterview.domain.repository.UserRepository

/**
 * DATA LAYER
 *
 * UserRepository Implementation (Concrete Implementation).
 * Responsible for deciding where data comes from (in this case, ApiService) and converting
 * data models (UserRemote) to domain models (User).
 * The ViewModel interacts with the interface (UserRepository), unaware of this concrete implementation.
 */
class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): List<User> {
        return try {
            val response = apiService.getUsers()
            // Maps API response to domain model
            response.users.map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
