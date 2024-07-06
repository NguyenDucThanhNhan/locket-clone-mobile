package com.myproject.locket_clone.viewmodel.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.model.BirthdayChangeRequest
import com.myproject.locket_clone.model.BirthdayChangeResponse
import com.myproject.locket_clone.model.ChangeEmailRequest
import com.myproject.locket_clone.model.ChangeEmailResponse
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.NameChangeResponse
import com.myproject.locket_clone.model.UpdateProfileImageResponse
import com.myproject.locket_clone.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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

    fun changeBirthday(token: String, userId: String, birthday: String, onSuccess: (BirthdayChangeResponse) -> Unit, onFailure: (String) -> Unit) {
        val request = BirthdayChangeRequest(birthday)
        val response = repository.changeBirthday(token, userId, request)
        response.enqueue(object : Callback<BirthdayChangeResponse> {
            override fun onResponse(call: Call<BirthdayChangeResponse>, response: Response<BirthdayChangeResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) } ?: onFailure("Response body is null")
                } else {
                    onFailure("Failed to change birthday: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BirthdayChangeResponse>, t: Throwable) {
                onFailure("Failed to change birthday: ${t.message}")
            }
        })
    }

    fun updateProfileImage(
        authorization: String,
        userId: String,
        image: MultipartBody.Part,
        onSuccess: (UpdateProfileImageResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.updateProfileImage(authorization, userId, image)
                response.enqueue(object : Callback<UpdateProfileImageResponse> {
                    override fun onResponse(call: Call<UpdateProfileImageResponse>, response: Response<UpdateProfileImageResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            onSuccess(response.body()!!)
                        } else {
                            onFailure("Failed to update profile image: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<UpdateProfileImageResponse>, t: Throwable) {
                        onFailure("Failed to update profile image: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                onFailure(e.message ?: "Unknown error")
            }
        }
    }


}