package com.uaialternativa.incommandroidhomechallengeinterview.data.repository

import com.uaialternativa.incommandroidhomechallengeinterview.data.model.UserRemote
import com.uaialternativa.incommandroidhomechallengeinterview.data.model.UserResponse
import com.uaialternativa.incommandroidhomechallengeinterview.data.remote.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * UNIT TEST
 *
 * This class tests `UserRepositoryImpl`.
 * 
 * Goal: Verify if the repository correctly calls the API and returns the mapped list of users.
 * We use Mockk to mock the `ApiService` behavior.
 */
class UserRepositoryImplTest {

    private val apiService = mockk<ApiService>()
    private val repository = UserRepositoryImpl(apiService)

    @Test
    fun `getUsers should return list of users when API call is successful`() = runTest {
        // Arrange
        val userRemote = UserRemote(
            id = 1, firstName = "Test", lastName = "User", email = "test@test.com",
            phone = null, avatarUrl = null, role = "Role", department = "Dept",
            isActive = true, joinedDate = "2023"
        )
        val response = UserResponse("success", 1, listOf(userRemote))
        
        // Mocking the API call to return our fake response
        coEvery { apiService.getUsers() } returns response

        // Act
        val result = repository.getUsers()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Test", result[0].firstName)
    }

    @Test
    fun `getUsers should return empty list when API call throws exception`() = runTest {
        // Arrange
        coEvery { apiService.getUsers() } throws Exception("Network error")

        // Act
        val result = repository.getUsers()

        // Assert
        assertEquals(0, result.size)
    }
}
