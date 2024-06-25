package com.myproject.locket_clone.api

import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateAccountService {
    @POST("/access/check-email")
    suspend fun validateEmail(@Body request: EmailValidationRequest): EmailValidationResponse
}
