package com.myproject.locket_clone.viewmodel.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.model.ChangePasswordResponse
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.model.SignupResponse
import com.myproject.locket_clone.repository.Repository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

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

    private val _emailValidationResponse = MutableLiveData<EmailValidationResponse>()
    val emailValidationResponse: LiveData<EmailValidationResponse> = _emailValidationResponse

    fun validateEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.pushChangePasswordEmail(email)
                _emailValidationResponse.value = response
            } catch (e: Exception) {
                _emailValidationResponse.value = EmailValidationResponse(
                    status = 500,
                    message = "Error: ${e.message}"
                )
            }
        }
    }

    private val _changePasswordResponse = MutableLiveData<ChangePasswordResponse>()
    val changePasswordResponse: LiveData<ChangePasswordResponse> = _changePasswordResponse

    fun changePassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.changePassword(email, password)
                _changePasswordResponse.value = response
            } catch (e: Exception) {
                _changePasswordResponse.value = ChangePasswordResponse(
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
}