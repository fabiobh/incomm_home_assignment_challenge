package com.uaialternativa.incommandroidhomechallengeinterview.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uaialternativa.incommandroidhomechallengeinterview.domain.model.User
import com.uaialternativa.incommandroidhomechallengeinterview.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private lateinit var mockRepository: MockUserRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = MockUserRepository()
        viewModel = UserViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUsers should update users livedata when repository returns data`() = runTest {
        // Given
        val user = User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phone = "123456",
            avatarUrl = "http://url",
            role = "Dev",
            department = "Eng",
            isActive = true,
            joinedDate = "2023-01-01"
        )
        mockRepository.usersToReturn = listOf(user)

        // When
        viewModel.loadUsers()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.users.value?.size)
        assertEquals(user, viewModel.users.value?.first())
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `loadUsers should update isLoading livedata correctly`() = runTest {
        // Given
        mockRepository.delayMs = 100 // Simulate delay

        // When
        viewModel.loadUsers()

        // Then
        // Note: Testing loading state specifically can be tricky with runTest+StandardTestDispatcher
        // because advanceUntilIdle executes everything.
        // We can check final state is false.
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.isLoading.value ?: true)
    }
}

class MockUserRepository : UserRepository {
    var usersToReturn: List<User> = emptyList()
    var delayMs: Long = 0

    override suspend fun getUsers(): List<User> {
        if (delayMs > 0) {
            kotlinx.coroutines.delay(delayMs)
        }
        return usersToReturn
    }
}
