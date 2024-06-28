package com.myproject.locket_clone.api

import com.myproject.locket_clone.model.Account
import com.myproject.locket_clone.model.ChangePasswordResponse
import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.SigninRequest
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.model.SignupRequest
import com.myproject.locket_clone.model.SignupResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface CreateAccountService {
    //Gui email len server de check xem co phai chinh chu hay khong danh cho tao tai khoan
    @POST("/access/check-email")
    suspend fun validateEmail(@Body request: EmailValidationRequest): EmailValidationResponse

    //Dang ky tai khoan
    @POST("/access/sign-up")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    //Dang nhap
    @POST("/access/sign-in")
    suspend fun signin(@Body request: SigninRequest): SigninResponse

    //Gui email len server de check xem co phai chinh chu hay khong danh cho doi mat khau
    @POST("/access/check-owner")
    suspend fun checkEmailOwner(@Body request: EmailValidationRequest): EmailValidationResponse

    //Doi mat khau
    @PATCH("/access/password")
    suspend fun changePassword(@Body request: Account): ChangePasswordResponse
}
