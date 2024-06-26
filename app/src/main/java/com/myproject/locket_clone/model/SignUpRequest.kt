package com.myproject.locket_clone.model

data class SignupRequest(
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val birthday: String
)