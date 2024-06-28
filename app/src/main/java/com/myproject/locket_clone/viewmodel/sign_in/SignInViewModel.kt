package com.myproject.locket_clone.viewmodel.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.repository.Repository
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: Repository) : ViewModel() {
    private val _signinResponse = MutableLiveData<SigninResponse>()
    val signinResponse: LiveData<SigninResponse> = _signinResponse

    fun signin(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.signin(email, password)
                _signinResponse.value = response
            } catch (e: Exception) {
                _signinResponse.value = SigninResponse(
                    status = 500,
                    message = "Error: ${e.message}"
                )
            }
        }
    }
}