package com.myproject.locket_clone.model

import java.io.Serializable

data class UserProfile(
    val email: String,
    val userId: String,
    val firstname: String,
    val lastname: String,
    val birthday: String,
    val profileImageUrl: String,
    val signInKey: String
) : Serializable