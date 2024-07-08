package com.myproject.locket_clone.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SignupResponse(
    val status: Int? = null,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: SignupMetadata? = null
)

data class SignupMetadata(
    val email: String,
    val password: String,
    val fullname: Fullname,
    val birthday: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String,
    val friends: List<Any>,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class Fullname(
    val firstname: String,
    val lastname: String
) : Serializable