package com.uaialternativa.incommandroidhomechallengeinterview.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.uaialternativa.incommandroidhomechallengeinterview.domain.model.User
import com.uaialternativa.incommandroidhomechallengeinterview.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UNIT TEST
 *
 * This class tests `UserViewModel`.
 * 
 * Goal: Verify if the ViewModel correctly updates the LiveData states (users, isLoading)
 * when interacting with the Repository.
 */
@ExperimentalCoroutinesApi
class UserViewModelTest {

    // Rule to swap the background executor used by Architecture Components with a synchronous one
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mockk<UserRepository>()
    private lateinit var viewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Set Main dispatcher to test dispatcher for Coroutines
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUsers should update users LiveData when repository returns data`() = runTest {
        // Arrange
        val user = User(
            id = 1, firstName = "Test", lastName = "User", email = "test@test.com",
            phone = null, avatarUrl = null, role = "Role", department = "Dept",
            isActive = true, joinedDate = "2023"
        )
        val userList = listOf(user)
        coEvery { repository.getUsers() } returns userList

        val observer = mockk<Observer<List<User>>>(relaxed = true)
        viewModel.users.observeForever(observer)

        // Act
        viewModel.loadUsers()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure coroutine completes

        // Assert
        verify { observer.onChanged(userList) }
    }

    @Test
    fun `loadUsers should update isLoading LiveData`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns emptyList()
        val observer = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isLoading.observeForever(observer)

        // Act
        viewModel.loadUsers()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        // Verify sequence: true (loading started) -> false (loading finished)
        verify { observer.onChanged(true) }
        verify { observer.onChanged(false) }
    }
}
