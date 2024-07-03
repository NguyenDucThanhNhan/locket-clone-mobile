package com.myproject.locket_clone.model

import java.io.Serializable

data class UserProfile(
    val email: String,
    val userId: String,
    val firstname: String,
    val lastname: String,
    val birthday: String,
    var profileImageUrl: String,
    val signInKey: String,
    val password: String
) : Serializable