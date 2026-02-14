package com.uaialternativa.incommandroidhomechallengeinterview.domain.repository

import com.uaialternativa.incommandroidhomechallengeinterview.domain.model.User

/**
 * DOMAIN LAYER
 *
 * Repository Interface.
 * Defines the contract for data access without exposing the data source (API, Database, etc.).
 * This enables the Dependency Inversion Principle (DIP), where domain depends on abstractions, not concrete implementations.
 */
interface UserRepository {
    suspend fun getUsers(): List<User>
}
