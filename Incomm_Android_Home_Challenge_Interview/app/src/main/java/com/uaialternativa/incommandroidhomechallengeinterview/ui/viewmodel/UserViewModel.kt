package com.uaialternativa.incommandroidhomechallengeinterview.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uaialternativa.incommandroidhomechallengeinterview.domain.model.User
import com.uaialternativa.incommandroidhomechallengeinterview.domain.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * PRESENTATION LAYER - MVVM
 *
 * ViewModel: Acts as an intermediary between the View (MainActivity) and the Domain Layer (UserRepository).
 * Responsible for managing UI state and presentation logic.
 * Survives configuration changes (e.g., screen rotation).
 */
class UserViewModel(private val repository: UserRepository) : ViewModel() {

    // LiveData to expose data state to the UI in an observable and lifecycle-aware manner
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    // LiveData to manage loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Initiates data fetching using Coroutines for asynchronous operation.
     * Interacts only with the repository (abstraction), maintaining decoupling.
     */
    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            val userList = repository.getUsers()
            _users.value = userList
            _isLoading.value = false
        }
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
