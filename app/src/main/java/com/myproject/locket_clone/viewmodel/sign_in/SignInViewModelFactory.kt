package com.myproject.locket_clone.viewmodel.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.repository.Repository

class SignInViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}