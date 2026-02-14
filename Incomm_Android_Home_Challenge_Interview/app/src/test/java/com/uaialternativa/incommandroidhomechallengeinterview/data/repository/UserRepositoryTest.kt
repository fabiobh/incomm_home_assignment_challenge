package com.uaialternativa.incommandroidhomechallengeinterview.data.repository

import com.uaialternativa.incommandroidhomechallengeinterview.data.model.UserResponse
import com.uaialternativa.incommandroidhomechallengeinterview.data.model.UserRemote
import com.uaialternativa.incommandroidhomechallengeinterview.data.remote.MockNetworkService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var repository: UserRepositoryImpl
    private lateinit var mockNetworkService: MockNetworkService

    @Before
    fun setUp() {
        mockNetworkService = MockNetworkService()
        repository = UserRepositoryImpl(mockNetworkService)
    }

    @Test
    fun `getUsers should return list of users when api call is successful`() = runBlocking {
        // Given
        val userRemote = UserRemote(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phone = "123-456-7890",
            avatarUrl = "http://example.com/avatar.png",
            role = "Developer",
            department = "Engineering",
            isActive = true,
            joinedDate = "2023-01-01"
        )
        val response = UserResponse("ok", 1, listOf(userRemote))
        mockNetworkService.response = response

        // When
        val result = repository.getUsers()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("John", result[0].firstName)
        assertEquals("Doe", result[0].lastName)
        assertEquals("john@example.com", result[0].email)
    }

    @Test
    fun `getUsers should return empty list when api call fails`() = runBlocking {
        // Given
        mockNetworkService.shouldThrowError = true

        // When
        val result = repository.getUsers()

        // Then
        assertEquals(0, result.size)
    }
}
