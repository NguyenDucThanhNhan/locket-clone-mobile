package com.myproject.locket_clone.model

import com.google.gson.annotations.SerializedName

data class SigninRequest(
    val email: String,
    val password: String
)

data class SigninResponse(
    val status: Int? = null,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: SigninMetadata? = null
)

data class SigninMetadata(
    val user: User,
    val signInKey: String
)

data class User(
    val _id: String,
    val email: String,
    val password: String,
    val fullname: Fullname,
    val birthday: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String,
    val friends: List<Any>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
