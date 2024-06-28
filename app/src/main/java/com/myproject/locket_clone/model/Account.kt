package com.myproject.locket_clone.model

data class Account(
    val email: String,
    val password: String
)

data class ChangePasswordResponse(
    val status: Int? = null,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: ChangePasswordMetadata? = null
)

data class ChangePasswordMetadata(
    val code: Int? = null
)