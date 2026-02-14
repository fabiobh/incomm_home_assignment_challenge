package com.uaialternativa.incommandroidhomechallengeinterview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uaialternativa.incommandroidhomechallengeinterview.data.remote.ApiService
import com.uaialternativa.incommandroidhomechallengeinterview.data.repository.UserRepositoryImpl
import com.uaialternativa.incommandroidhomechallengeinterview.databinding.ActivityMainBinding
import com.uaialternativa.incommandroidhomechallengeinterview.ui.adapter.UserAdapter
import com.uaialternativa.incommandroidhomechallengeinterview.ui.viewmodel.UserViewModel
import com.uaialternativa.incommandroidhomechallengeinterview.ui.viewmodel.UserViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * PRESENTATION LAYER - MVVM
 *
 * View (Activity): Responsible only for rendering the user interface and capturing events.
 * It observes the ViewModel (LiveData) and reacts to state changes, updating the UI.
 * Does not contain business logic.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
        observeViewModel()

        // Requests ViewModel to load data
        viewModel.loadUsers()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    /**
     * Dependency Configuration (Manual Dependency Injection)
     * Here we are building the dependency graph:
     * Retrofit -> ApiService -> UserRepository -> ViewModel
     * In larger apps, we use Hilt or Koin for this.
     */
    private fun setupViewModel() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oneqmock.proxy.beeceptor.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val repository = UserRepositoryImpl(apiService)
        val factory = UserViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    /**
     * Observer Pattern: The View observes the ViewModel.
     * When the ViewModel updates LiveData, the View is automatically notified.
     */
    private fun observeViewModel() {
        viewModel.users.observe(this) { users ->
            adapter.setUsers(users)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }
}