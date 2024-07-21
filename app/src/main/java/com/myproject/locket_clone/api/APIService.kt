package com.myproject.locket_clone.api

import com.myproject.locket_clone.model.Account
import com.myproject.locket_clone.model.BirthdayChangeRequest
import com.myproject.locket_clone.model.BirthdayChangeResponse
import com.myproject.locket_clone.model.ChangeEmailRequest
import com.myproject.locket_clone.model.ChangeEmailResponse
import com.myproject.locket_clone.model.ChangePasswordResponse
import com.myproject.locket_clone.model.CreateFeedResponse
import com.myproject.locket_clone.model.EmailValidationRequest
import com.myproject.locket_clone.model.EmailValidationResponse
import com.myproject.locket_clone.model.GetCertainFeedsResponse
import com.myproject.locket_clone.model.Home
import com.myproject.locket_clone.model.NameChangeRequest
import com.myproject.locket_clone.model.NameChangeResponse
import com.myproject.locket_clone.model.SigninRequest
import com.myproject.locket_clone.model.SigninResponse
import com.myproject.locket_clone.model.SignupRequest
import com.myproject.locket_clone.model.SignupResponse
import com.myproject.locket_clone.model.UpdateProfileImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    //Doi ngay sinh
    @PATCH("/account/birthday")
    fun changeBirthday(
        @Header("authorization") token: String,
        @Header("user-id") userId: String,
        @Body request: BirthdayChangeRequest
    ): Call<BirthdayChangeResponse>

    //Doi avatar
    @PATCH("account/profile-image")
    @Multipart
    fun updateProfileImage(
        @Header("authorization") token: String,
        @Header("user-id") userId: String,
        @Part image: MultipartBody.Part
    ): Call<UpdateProfileImageResponse>

    //Tim kiem nguoi dung
    @GET("/search/{searchValue}")
    fun searchUser(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Path("searchValue") searchValue: String
    ): Call<Home.SearchResponse>

    //Gui loi moi ket ban
    @POST("account/friend/send-invite")
    suspend fun sendInvite(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Body body: Home.FriendIdRequest
    ): Home.SendInviteResponse


    //Chap nhan loi moi ket ban
    @POST("account/friend/accept")
    suspend fun acceptInvite(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Body body: Home.FriendIdRequest
    ): Home.AcceptInviteResponse

    //Xoa ban
    @POST("account/friend/remove")
    suspend fun removeFriend(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Body body: Home.FriendIdRequest
    ): Home.RemoveFriendResponse

    //Lay thong tin cua user
    @GET("search/user/{userId}")
    suspend fun getUserInfo(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Path("userId") targetUserId: String
    ): Home.UserInfoResponse

    //Xoa loi moi ket ban
    @POST("account/friend/remove-invite")
    suspend fun removeInvite(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Body removeInviteRequest: Home.RemoveInviteRequest
    ): Home.RemoveInviteResponse

    //Dang bai
    @Multipart
    @POST("feed/create")
    suspend fun createFeed(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Part("description") description: RequestBody,
        @Part("visibility") visibility: RequestBody,
        @Part image: MultipartBody.Part
    ): CreateFeedResponse

    //Lay cac bai dang de hien thi cho user
    @GET("feed/certain/{searchId}")
    suspend fun getCertainFeeds(
        @Header("authorization") authorization: String,
        @Header("user-id") userId: String,
        @Path("searchId") searchId: String,
        @Query("page") page: Int
    ): GetCertainFeedsResponse
}
