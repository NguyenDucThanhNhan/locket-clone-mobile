package com.myproject.locket_clone.api

import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.SigninRequest
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.model.SignupRequest
import com.myproject.locket_clone.model.SignupResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateAccountService {
    @POST("/access/check-email")
    suspend fun validateEmail(@Body request: EmailValidationRequest): EmailValidationResponse

    @POST("/access/sign-up")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    @POST("/access/sign-in")
    suspend fun signin(@Body request: SigninRequest): SigninResponse

    @POST("/access/check-owner")
    suspend fun checkEmailOwner(@Body request: EmailValidationRequest): EmailValidationResponse
}
