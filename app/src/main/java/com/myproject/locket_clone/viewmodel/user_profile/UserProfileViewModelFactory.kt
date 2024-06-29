package com.myproject.locket_clone.viewmodel.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.repository.Repository

class UserProfileViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}