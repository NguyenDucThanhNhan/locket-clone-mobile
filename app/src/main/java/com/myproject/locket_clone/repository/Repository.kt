package com.myproject.locket_clone.repository

import com.myproject.locket_clone.api.RetrofitInstance
import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.SignupRequest
import com.myproject.locket_clone.model.SignupResponse

class Repository {
    suspend fun pushCreateAccountEmail(email: String): EmailValidationResponse {
        val request = EmailValidationRequest(email)
        return RetrofitInstance.api.validateEmail(request)
    }

    suspend fun signup(email: String, password: String, firstname: String, lastname: String, birthday: String): SignupResponse {
        val request = SignupRequest(email, password, firstname, lastname, birthday)
        return RetrofitInstance.api.signup(request)
    }
}
