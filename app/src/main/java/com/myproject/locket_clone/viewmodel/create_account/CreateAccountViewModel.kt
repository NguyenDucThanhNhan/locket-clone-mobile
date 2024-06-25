package com.myproject.locket_clone.viewmodel.create_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.repository.Repository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class CreateAccountViewModel(private val repository: Repository) : ViewModel() {

    private val _emailValidationResponse = MutableLiveData<EmailValidationResponse>()
    val emailValidationResponse: LiveData<EmailValidationResponse> = _emailValidationResponse

    fun validateEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.pushCreateAccountEmail(email)
                _emailValidationResponse.value = response
            } catch (e: Exception) {
                _emailValidationResponse.value = EmailValidationResponse(
                    status = 500,
                    message = "Error: ${e.message}"
                )
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isValidSixDigitNumber(code: String): Boolean {
        val sixDigitPattern = "^[0-9]{6}$"
        return code.matches(sixDigitPattern.toRegex())
    }

    fun isValidCode(code: String, trueCode: String): Boolean{
        return code==trueCode
    }
}
