package com.myproject.locket_clone.repository

import com.myproject.locket_clone.api.RetrofitInstance
import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse

class Repository {
    suspend fun pushCreateAccountEmail(email: String): EmailValidationResponse {
        val request = EmailValidationRequest(email)
        return RetrofitInstance.api.validateEmail(request)
    }
}
