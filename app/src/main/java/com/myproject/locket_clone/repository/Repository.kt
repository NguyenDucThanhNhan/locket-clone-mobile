package com.myproject.locket_clone.repository

import com.myproject.locket_clone.api.RetrofitInstance
import com.myproject.locket_clone.model.Account
import com.myproject.locket_clone.model.BirthdayChangeRequest
import com.myproject.locket_clone.model.BirthdayChangeResponse
import com.myproject.locket_clone.model.ChangeEmailRequest
import com.myproject.locket_clone.model.ChangeEmailResponse
import com.myproject.locket_clone.model.ChangePasswordResponse
import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.Home
import com.myproject.locket_clone.model.NameChangeRequest
import com.myproject.locket_clone.model.NameChangeResponse
import com.myproject.locket_clone.model.SigninRequest
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.model.SignupRequest
import com.myproject.locket_clone.model.SignupResponse
import com.myproject.locket_clone.model.UpdateProfileImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class Repository {
    suspend fun pushCreateAccountEmail(email: String): EmailValidationResponse {
        val request = EmailValidationRequest(email)
        return RetrofitInstance.api.validateEmail(request)
    }

    suspend fun signup(email: String, password: String, firstname: String, lastname: String, birthday: String): SignupResponse {
        val request = SignupRequest(email, password, firstname, lastname, birthday)
        return RetrofitInstance.api.signup(request)
    }

    suspend fun signin(email: String, password: String): SigninResponse {
        val request = SigninRequest(email, password)
        return RetrofitInstance.api.signin(request)
    }

    suspend fun pushChangePasswordEmail(email: String): EmailValidationResponse {
        val request = EmailValidationRequest(email)
        return RetrofitInstance.api.checkEmailOwner(request)
    }

    suspend fun changePassword(email: String, password: String): ChangePasswordResponse {
        val request = Account(email, password)
        return RetrofitInstance.api.changePassword(request)
    }

    fun changeName(token: String, userId: String, firstname: String, lastname: String): Call<NameChangeResponse> {
        val request = NameChangeRequest(firstname, lastname)
        return RetrofitInstance.api.changeName(token, userId, request)
    }

    fun signOut(token: String, userId: String): Call<Void> {
        return RetrofitInstance.api.signOut(token, userId)
    }

    fun changeEmail(token: String, userId: String, request: ChangeEmailRequest): Call<ChangeEmailResponse> {
        return RetrofitInstance.api.changeEmail(token, userId, request)
    }

    fun changeBirthday(token: String, userId: String, request: BirthdayChangeRequest): Call<BirthdayChangeResponse> {
        return RetrofitInstance.api.changeBirthday(token, userId, request)
    }

    fun updateProfileImage(authorization: String, userId: String, image: MultipartBody.Part): Call<UpdateProfileImageResponse> {
        return RetrofitInstance.api.updateProfileImage(authorization, userId, image)
    }

    fun searchUser(authorization: String, userId: String, searchValue: String): Call<Home.SearchResponse> {
        return RetrofitInstance.api.searchUser(authorization, userId, searchValue)
    }

    suspend fun sendInvite(authorization: String, userId: String, friendId: String): Home.SendInviteResponse {
        return RetrofitInstance.api.sendInvite(authorization, userId, Home.FriendIdRequest(friendId))
    }

}
