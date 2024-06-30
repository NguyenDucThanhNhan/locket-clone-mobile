package com.myproject.locket_clone.api

import com.myproject.locket_clone.model.Account
import com.myproject.locket_clone.model.ChangeEmailRequest
import com.myproject.locket_clone.model.ChangeEmailResponse
import com.myproject.locket_clone.model.ChangePasswordResponse
import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.NameChangeRequest
import com.myproject.locket_clone.model.NameChangeResponse
import com.myproject.locket_clone.model.SigninRequest
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.model.SignupRequest
import com.myproject.locket_clone.model.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface APIService {
    //Gui email len server de check xem co phai chinh chu hay khong danh cho tao tai khoan va doi email
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

    //Doi ten
    @PATCH("/account/name")
    fun changeName(
        @Header("authorization") token: String,
        @Header("user-id") userId: String,
        @Body request: NameChangeRequest
    ): Call<NameChangeResponse>

    //Dang xuat
    @POST("/access/signout")
    fun signOut(
        @Header("authorization") token: String,
        @Header("user-id") userId: String
    ): Call<Void>

    //Doi email
    @PATCH("/account/email")
    fun changeEmail(
        @Header("authorization") token: String,
        @Header("user-id") userId: String,
        @Body request: ChangeEmailRequest
    ): Call<ChangeEmailResponse>
}
