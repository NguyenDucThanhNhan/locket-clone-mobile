package com.myproject.locket_clone.viewmodel.create_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.repository.Repository

class CreateAccountViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateAccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
