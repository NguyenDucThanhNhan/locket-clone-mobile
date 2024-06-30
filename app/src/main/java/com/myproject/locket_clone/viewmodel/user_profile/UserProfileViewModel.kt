package com.myproject.locket_clone.viewmodel.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.model.ChangeEmailRequest
import com.myproject.locket_clone.model.ChangeEmailResponse
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.NameChangeResponse
import com.myproject.locket_clone.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class UserProfileViewModel(private val repository: Repository) : ViewModel() {
    val nameChangeResponse: MutableLiveData<NameChangeResponse> = MutableLiveData()

    fun changeName(token: String, userId: String, firstname: String, lastname: String) {
        val response = repository.changeName(token, userId, firstname, lastname)
        response.enqueue(object : Callback<NameChangeResponse> {
            override fun onResponse(call: Call<NameChangeResponse>, response: Response<NameChangeResponse>) {
                nameChangeResponse.value = response.body()
            }

            override fun onFailure(call: Call<NameChangeResponse>, t: Throwable) {

            }
        })
    }

    fun signOut(token: String, userId: String) {
        val response = repository.signOut(token, userId)
        response.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

            }
        })
    }

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

    fun changeEmail(token: String, userId: String, email: String, onSuccess: (ChangeEmailResponse) -> Unit, onFailure: (String) -> Unit) {
        val request = ChangeEmailRequest(email)
        val response = repository.changeEmail(token, userId, request)
        response.enqueue(object : Callback<ChangeEmailResponse> {
            override fun onResponse(call: Call<ChangeEmailResponse>, response: Response<ChangeEmailResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) } ?: onFailure("Response body is null")
                } else {
                    onFailure("Failed to change email: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ChangeEmailResponse>, t: Throwable) {
                onFailure("Failed to change email: ${t.message}")
            }
        })
    }
}