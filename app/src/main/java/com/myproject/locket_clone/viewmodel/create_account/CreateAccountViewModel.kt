package com.myproject.locket_clone.viewmodel.create_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.SignupResponse
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

    fun isValidPassword(password: String): Boolean {
        val minLength = 8
        val hasDigit = password.any { it.isDigit() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return password.length >= minLength && hasDigit && hasUpperCase && hasSpecialChar
    }

    fun isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    private val _signupResponse = MutableLiveData<SignupResponse>()
    val signupResponse: LiveData<SignupResponse> = _signupResponse

    fun signup(email: String, password: String, firstname: String, lastname: String, birthday: String) {
        viewModelScope.launch {
            try {
                val response = repository.signup(email, password, firstname, lastname, birthday)
                _signupResponse.value = response
            } catch (e: Exception) {
                _signupResponse.value = SignupResponse(
                    status = 500,
                    message = "Error: ${e.message}"
                )
            }
        }
    }
}
